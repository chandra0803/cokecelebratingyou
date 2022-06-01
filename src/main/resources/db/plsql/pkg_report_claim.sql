CREATE OR REPLACE PACKAGE pkg_report_claim
  IS
--
-- To modify this template, edit file PKGSPEC.TXT in TEMPLATE
-- directory of SQL Navigator
--
-- Purpose: Briefly explain the functionality of the package
--
-- MODIFICATION HISTORY
-- Person      Date         Comments
-- ---------   ------      ------------------------------------------
-- Arun S      08/27/2012  G5 report changes
--                         added fnc_eligibile_submitter, fnc_actual_submitter
--                         and fnc_item_count
-- Ravi Dhanekula 10/05/2012 Changed to accept multi-select option for promotion-ids and departments
-- Chidamba       12/04/2012 Defect ID 711 - Adding p_in_promo_status to get appropriate count 

   -- Enter package declarations as shown below

  FUNCTION fnc_eligibile_submitter
    ( p_in_promotion_id IN VARCHAR2,
      p_in_promo_status IN VARCHAR2, --12/04/2012
      p_in_node_id      IN NUMBER,
      p_in_record_type  IN VARCHAR2,
      p_in_pax_status   IN VARCHAR2,
      p_in_job_code     IN VARCHAR2,
      p_in_department   IN VARCHAR2,
      p_in_promo_type   IN VARCHAR2 DEFAULT 'product_claim')
    RETURN  NUMBER;

  FUNCTION fnc_actual_submitter
    ( p_in_promotion_id IN VARCHAR2,
      p_in_promo_status IN VARCHAR2, --12/04/2012
      p_in_node_id      IN NUMBER,
      p_in_record_type  IN VARCHAR2,
      p_in_claim_status IN VARCHAR2,
      p_in_pax_status   IN VARCHAR2,
      p_in_job_code     IN VARCHAR2,
      p_in_department   IN VARCHAR2,
      p_in_from_date    IN DATE,
      p_in_to_date      IN DATE
    )
    RETURN  NUMBER;

  FUNCTION fnc_item_count
    ( p_in_promotion_id         IN VARCHAR2,
      p_in_promo_status         IN VARCHAR2, --12/04/2012
      p_in_node_id              IN NUMBER,
      p_in_record_type          IN VARCHAR2,
      p_in_approval_status_type IN VARCHAR2,
      p_in_claim_status         IN VARCHAR2,
      p_in_pax_status           IN VARCHAR2,
      p_in_job_code             IN VARCHAR2,
      p_in_department           IN VARCHAR2,
      p_in_from_date            IN DATE,
      p_in_to_date              IN DATE)
    RETURN  NUMBER;

  FUNCTION fnc_submitter_item_count
    ( p_in_promotion_id         IN VARCHAR2,
      p_in_promo_status         IN VARCHAR2, --12/04/2012
      p_in_submitter_id         IN NUMBER,
      p_in_approval_status_type IN VARCHAR2,
      p_in_claim_status   IN VARCHAR2,
      p_in_pax_status           IN VARCHAR2,
      p_in_job_code             IN VARCHAR2,
      p_in_department           IN VARCHAR2,
      p_in_from_date            IN DATE,
      p_in_to_date              IN DATE)
    RETURN  NUMBER;

   PROCEDURE prc_claim_detail
    (p_user_id              IN NUMBER,
     p_start_date           IN DATE, 
     p_end_date             IN DATE, 
     p_return_code          OUT NUMBER,
     p_error_message        OUT VARCHAR2);

   PROCEDURE prc_claim_summary
    (p_user_id              IN NUMBER,
     p_start_date           IN DATE, 
     p_end_date             IN DATE, 
     p_return_code          OUT NUMBER,
     p_error_message        OUT VARCHAR2);

   PROCEDURE p_claim_product_summary;

END; -- Package spec
/
CREATE OR REPLACE PACKAGE BODY pkg_report_claim
IS

FUNCTION fnc_eligibile_submitter
  ( p_in_promotion_id IN VARCHAR2,
    p_in_promo_status IN VARCHAR2, --12/04/2012
    p_in_node_id      IN NUMBER,
    p_in_record_type  IN VARCHAR2,    
    p_in_pax_status   IN VARCHAR2,
    p_in_job_code     IN VARCHAR2,
    p_in_department   IN VARCHAR2,
    p_in_promo_type   IN VARCHAR2 DEFAULT 'product_claim')
  RETURN  NUMBER IS

/*******************************************************************************
   Purpose:  Get the number of eligible submitter count which will be used in 
             online report

   Person          Date        Comments
   -----------     ----------  -------------------------------------------------
   Arun S          08/27/2012  Initial Creation
   Ravi Dhanekula  10/05/2012  Changed to accept multi-select option for promotion-ids and departments
   Chidamba        12/04/2012  Defect ID 711 - Adding p_in_promo_status to get appropriate count
*******************************************************************************/

  --Variables
  v_process_name     execution_log.process_name%type  := 'fnc_eligibile_submitter';
  v_release_level    execution_log.release_level%type := '1';
  v_eligible_cnt     NUMBER;

BEGIN

   IF p_in_record_type = 'team' THEN -- team or node
     -- check to see if we have to join the particpant employer records.
     IF p_in_job_code IS NULL AND p_in_department IS NULL THEN  

       SELECT COUNT(DISTINCT participant_id)
         INTO v_eligible_cnt
         FROM rpt_pax_promo_eligibility r,
              promo_product_claim ppc,
              participant p,
              promotion pr  --12/04/2012
        WHERE r.promotion_id     = NVL(ppc.parent_promotion_id, ppc.promotion_id)
          AND r.participant_id   = p.user_id
          AND pr.promotion_id = r.promotion_id  --12/04/2012
          AND pr.promotion_status = NVL(p_in_promo_status,pr.promotion_status)  --12/04/2012
          AND ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND ppc.promotion_id IN  (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))
          AND r.node_id          = p_in_node_id
          AND r.giver_recvr_type IN ('giver','submitter')
          AND p.status           = NVL(p_in_pax_status,p.status)
          AND r.promotion_id     IN (SELECT promotion_id FROM promotion WHERE promotion_type = NVL(p_in_promo_type,promotion_type));
     ELSE 
       SELECT COUNT(DISTINCT participant_id)
         INTO v_eligible_cnt
         FROM rpt_pax_promo_eligibility r,
              promo_product_claim ppc,
              participant p,
              vw_curr_pax_employer vcpe,
              promotion pr  --12/04/2012
        WHERE r.promotion_id     = NVL(ppc.parent_promotion_id, ppc.promotion_id)
          AND r.participant_id   = p.user_id
          AND pr.promotion_id    = r.promotion_id  --12/04/2012
          AND pr.promotion_status = NVL(p_in_promo_status,pr.promotion_status)  --12/04/2012
          AND p.user_id          = vcpe.user_id
          AND ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND ppc.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))
          AND r.node_id          = p_in_node_id
          AND r.giver_recvr_type IN ('giver','submitter')
          AND p.status           = NVL(p_in_pax_status,p.status)          
          AND ((p_in_job_code IS NULL) OR (p_in_job_code IS NOT NULL AND vcpe.position_type = p_in_job_code))
          --AND vcpe.department_type = NVL(p_in_department,vcpe.department_type)
          AND ((p_in_department IS NULL) OR (p_in_department is NOT NULL AND vcpe.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_department)))))
          AND r.promotion_id IN ( SELECT promotion_id FROM PROMOTION WHERE promotion_type = NVL(p_in_promo_type,promotion_type));
     END IF ;

   ELSE -- team or node
     -- check to see if we have to join the particpant employer records.
     IF p_in_job_code IS NULL AND p_in_department IS NULL THEN  
       -- ignore the pax employer record
       SELECT COUNT(DISTINCT participant_id)
         INTO v_eligible_cnt
         FROM rpt_pax_promo_eligibility r,
              promo_product_claim ppc,
              participant p,
              promotion pr  --12/04/2012
        WHERE r.promotion_id     = NVL(ppc.parent_promotion_id, ppc.promotion_id)
          AND r.participant_id   = p.user_id
          AND pr.promotion_id = r.promotion_id  --12/04/2012
          AND pr.promotion_status = NVL(p_in_promo_status,pr.promotion_status)  --12/04/2012
          AND ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND ppc.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))
          AND r.node_id  IN
               (SELECT node_id
                  FROM rpt_hierarchy
               CONNECT BY PRIOR node_id = parent_node_id
                 START WITH node_id = p_in_node_id)
          AND r.giver_recvr_type IN ('giver','submitter')
          AND p.status           = NVL(p_in_pax_status,p.status)
          AND r.promotion_id     IN (SELECT promotion_id FROM promotion WHERE promotion_type = NVL(p_in_promo_type,promotion_type));
     ELSE 
       -- include pax employer record
       SELECT COUNT(DISTINCT participant_id)
         INTO v_eligible_cnt
         FROM rpt_pax_promo_eligibility r,
              promo_product_claim ppc,
              participant p,
              vw_curr_pax_employer vcpe,
              promotion pr  --12/04/2012
        WHERE r.promotion_id     = NVL(ppc.parent_promotion_id, ppc.promotion_id)
          AND r.participant_id   = p.user_id
          AND pr.promotion_id = r.promotion_id  --12/04/2012
          AND pr.promotion_status = NVL(p_in_promo_status,pr.promotion_status)  --12/04/2012
          AND p.user_id          = vcpe.user_id          
          AND ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND ppc.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))
          AND r.node_id IN
               (SELECT node_id
                  FROM rpt_hierarchy
               CONNECT BY PRIOR node_id = parent_node_id
                 START WITH node_id = p_in_node_id)
          AND r.giver_recvr_type IN ('giver','submitter')
          AND p.status           = NVL(p_in_pax_status,p.status)            
          AND ((p_in_job_code IS NULL) OR (p_in_job_code IS NOT NULL AND vcpe.position_type = p_in_job_code))
          AND ((p_in_department IS NULL) OR (p_in_department is NOT NULL AND vcpe.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_department))))) 
          AND r.promotion_id     IN (SELECT promotion_id FROM promotion WHERE promotion_type = NVL(p_in_promo_type,promotion_type));
     END IF;   
   END IF ; -- team or node
   
  RETURN v_eligible_cnt;
   
EXCEPTION
   WHEN OTHERS THEN
     prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                            'Claim Promo: '||p_in_promotion_id||' Node: '||p_in_node_id||'  '||
                            '  Record Type: '||p_in_record_type||
                            '  Pax Status: '||p_in_pax_status||'  Job Code: '||p_in_job_code||
                            '  Department: '||p_in_department||' --> '||SQLERRM,null);
       v_eligible_cnt := 0;
       RETURN v_eligible_cnt;
END; -- fnc_eligibility_count


FUNCTION fnc_actual_submitter
    ( p_in_promotion_id IN VARCHAR2,
      p_in_promo_status IN VARCHAR2, --12/04/2012
      p_in_node_id      IN NUMBER,
      p_in_record_type  IN VARCHAR2,
      p_in_claim_status IN VARCHAR2,
      p_in_pax_status   IN VARCHAR2,
      p_in_job_code     IN VARCHAR2,
      p_in_department   IN VARCHAR2,
      p_in_from_date    IN DATE,
      p_in_to_date      IN DATE)
    RETURN  NUMBER IS

/*******************************************************************************
   Purpose:  Get the number of actual submitter count which will be used in 
             online report

   Person          Date        Comments
   -----------     ----------  -------------------------------------------------
   Arun S          08/27/2012  Initial Creation
   Ravi Dhanekula 10/05/2012 Changed to accept multi-select option for promotion-ids and departments
   Chidamba        12/04/2012 Defect ID 711 - Adding p_in_promo_status to get appropriate count
   Chidamba        01/17/2014 Bug# 51111 - Use NVL for "Show All" filter p_in_claim_status
*******************************************************************************/

  --Variables
  v_process_name      execution_log.process_name%type  := 'fnc_actual_submitter';
  v_release_level     execution_log.release_level%type := '1';
  v_actual_cnt        NUMBER;

BEGIN

  IF p_in_record_type = 'team' THEN

    SELECT COUNT(DISTINCT submitter_user_id)
      INTO v_actual_cnt
      FROM rpt_claim_detail r,
           promotion p  --12/04/2012
     WHERE node_id                = p_in_node_id
       AND p.promotion_id = r.promotion_id  --12/04/2012
       AND p.promotion_status = NVL(p_in_promo_status,p.promotion_status)  --12/04/2012
       AND ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND p.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))
       AND submitter_pax_status   = NVL(p_in_pax_status,submitter_pax_status)
       AND ((p_in_job_code IS NULL) OR (p_in_job_code IS NOT NULL AND submitter_job_position = p_in_job_code))
       AND r.claim_id IS NOT NULL
       AND r.claim_status = NVL(p_in_claim_status,r.claim_status)        --01/17/2014 Bug# 51111
--       AND submitter_department   = NVL(p_in_department,submitter_department)
       AND ((p_in_department IS NULL) OR (p_in_department is NOT NULL AND submitter_department IN (SELECT * FROM TABLE(get_array_varchar(p_in_department)))))
       AND TRUNC(NVL(date_submitted,SYSDATE)) BETWEEN p_in_from_date AND p_in_to_date;

  ELSE

    SELECT COUNT(DISTINCT submitter_user_id)
      INTO v_actual_cnt
      FROM rpt_claim_detail r,
           promotion p  --12/04/2012
     WHERE ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND p.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))
       AND submitter_pax_status   = NVL(p_in_pax_status,submitter_pax_status)
       AND ((p_in_job_code IS NULL) OR (p_in_job_code IS NOT NULL AND submitter_job_position = p_in_job_code))
       AND p.promotion_id = r.promotion_id  --12/04/2012
       AND p.promotion_status = NVL(p_in_promo_status,p.promotion_status)  --12/04/2012
      -- AND submitter_department   = NVL(p_in_department,submitter_department)
       AND r.claim_status = NVL(p_in_claim_status,r.claim_status)        --01/17/2014 Bug# 51111
       AND ((p_in_department IS NULL) OR (p_in_department is NOT NULL AND submitter_department IN (SELECT * FROM TABLE(get_array_varchar(p_in_department)))))
       AND TRUNC(NVL(date_submitted,SYSDATE)) BETWEEN p_in_from_date AND p_in_to_date
       AND node_id IN (SELECT node_id
                         FROM rpt_hierarchy
                      CONNECT BY PRIOR node_id = parent_node_id
                        START WITH node_id     = p_in_node_id);
  
  END IF;  
  
  RETURN v_actual_cnt;
  
EXCEPTION
  WHEN OTHERS THEN
    prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                            'Claim Promo: '||p_in_promotion_id||' Node: '||p_in_node_id||'  '||
                            '  From Date: '||p_in_from_date||'  To Date: '||p_in_to_date||
                            '  Record Type: '||p_in_record_type||
                            '  Pax Status: '||p_in_pax_status||'  Job Code: '||p_in_job_code||
                            '  Department: '||p_in_department||' --> '||SQLERRM,null);

    v_actual_cnt := 0;
    RETURN v_actual_cnt;

END;  --fnc_actual_submitter

FUNCTION fnc_item_count
    ( p_in_promotion_id         IN VARCHAR2,
      p_in_promo_status         IN VARCHAR2, --12/04/2012
      p_in_node_id              IN NUMBER,
      p_in_record_type          IN VARCHAR2,
      p_in_approval_status_type IN VARCHAR2,
      p_in_claim_status         IN VARCHAR2,
      p_in_pax_status           IN VARCHAR2,
      p_in_job_code             IN VARCHAR2,
      p_in_department           IN VARCHAR2,
      p_in_from_date            IN DATE,
      p_in_to_date              IN DATE)
    RETURN  NUMBER IS

/*******************************************************************************
   Purpose:  Get the number of Item count based on item status which will be used in 
             online report

   Person          Date        Comments
   -----------     ----------  -------------------------------------------------
   Arun S          08/27/2012  Initial Creation
   Ravi Dhanekula 10/05/2012 Changed to accept multi-select option for promotion-ids and departments
   Chidamba        12/04/2012  Defect ID 711 - Adding p_in_promo_status to get appropriate count
*******************************************************************************/

  --Variables
  v_process_name      execution_log.process_name%type  := 'fnc_item_count';
  v_release_level     execution_log.release_level%type := '1';
  v_item_cnt          NUMBER;
    
BEGIN

  IF p_in_record_type = 'team' THEN

    SELECT NVL(SUM(cp.product_qty),0)
      INTO v_item_cnt
      FROM claim_product cp,
           claim_item ci,
           rpt_claim_detail rcd,
           promotion p
     WHERE rcd.claim_id            = ci.claim_id
       AND ci.claim_item_id        = cp.claim_item_id
       AND p.promotion_id = rcd.promotion_id  --12/04/2012
       AND p.promotion_status = NVL(p_in_promo_status,p.promotion_status)  --12/04/2012
       AND ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND p.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))
       AND rcd.node_id             = p_in_node_id
       AND ci.approval_status_type = p_in_approval_status_type
       AND ((p_in_claim_status IS NULL) OR (p_in_claim_status IS NOT NULL AND rcd.claim_status = p_in_claim_status))
       AND submitter_pax_status    = NVL(p_in_pax_status,submitter_pax_status)
       AND ((p_in_job_code IS NULL) OR (p_in_job_code IS NOT NULL AND submitter_job_position = p_in_job_code))
       AND ((p_in_department IS NULL) OR (p_in_department is NOT NULL AND submitter_department IN (SELECT * FROM TABLE(get_array_varchar(p_in_department)))))
       
       AND TRUNC(NVL(date_submitted,SYSDATE)) BETWEEN p_in_from_date AND p_in_to_date;

  ELSE

    SELECT NVL(SUM(cp.product_qty),0)
      INTO v_item_cnt
      FROM claim_product cp,
           claim_item ci,
           rpt_claim_detail rcd ,
           promotion p
     WHERE rcd.claim_id            = ci.claim_id
       AND ci.claim_item_id        = cp.claim_item_id
       AND p.promotion_id = rcd.promotion_id  --12/04/2012
       AND p.promotion_status = NVL(p_in_promo_status,p.promotion_status)  --12/04/2012
       AND ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND p.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))
       AND ci.approval_status_type = p_in_approval_status_type
       AND ((p_in_claim_status IS NULL) OR (p_in_claim_status IS NOT NULL AND rcd.claim_status = p_in_claim_status))
       AND submitter_pax_status    = NVL(p_in_pax_status,submitter_pax_status)
       AND ((p_in_job_code IS NULL) OR (p_in_job_code IS NOT NULL AND submitter_job_position = p_in_job_code))
       AND ((p_in_department IS NULL) OR (p_in_department is NOT NULL AND submitter_department IN (SELECT * FROM TABLE(get_array_varchar(p_in_department)))))
       AND TRUNC(NVL(date_submitted,SYSDATE)) BETWEEN p_in_from_date AND p_in_to_date
       AND rcd.node_id             IN (SELECT node_id
                                         FROM rpt_hierarchy
                                      CONNECT BY PRIOR node_id = parent_node_id
                                        START WITH node_id     = p_in_node_id);
  
  END IF;

  RETURN v_item_cnt;
  
EXCEPTION
   WHEN OTHERS THEN
     prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                            'Claim Promo: '||p_in_promotion_id||' Node: '||p_in_node_id||'  '||
                            '  Pax Status: '||p_in_pax_status||'  Job Code: '||p_in_job_code||
                            '  Department: '||p_in_department||' --> '||SQLERRM,null);
       v_item_cnt := 0;
       RETURN v_item_cnt;
END;    --fnc_item_count


  FUNCTION fnc_submitter_item_count
    ( p_in_promotion_id         IN VARCHAR2,
      p_in_promo_status         IN VARCHAR2, --12/04/2012
      p_in_submitter_id         IN NUMBER,
      p_in_approval_status_type IN VARCHAR2,
      p_in_claim_status   IN VARCHAR2,
      p_in_pax_status           IN VARCHAR2,
      p_in_job_code             IN VARCHAR2,
      p_in_department           IN VARCHAR2,
      p_in_from_date            IN DATE,
      p_in_to_date              IN DATE)
    RETURN  NUMBER
    IS

/*******************************************************************************
   Purpose:  Get the number of Item count based on item status which will be used in 
             online report

   Person          Date        Comments
   -----------     ----------  -------------------------------------------------
   Arun S          08/27/2012  Initial Creation
   Ravi Dhanekula 10/05/2012 Changed to accept multi-select option for promotion-ids and departments
  Chidamba        12/04/2012  Defect ID 711 - Adding p_in_promo_status to get appropriate count
*******************************************************************************/

  --Variables
  v_process_name      execution_log.process_name%type  := 'fnc_submitter_item_count';
  v_release_level     execution_log.release_level%type := '1';
  v_item_cnt          NUMBER;
    
BEGIN

  SELECT NVL(SUM(cp.product_qty),0)
    INTO v_item_cnt
    FROM claim_product cp,
         claim_item ci,
         rpt_claim_detail rcd ,
         promotion p
   WHERE rcd.claim_id            = ci.claim_id
     AND ci.claim_item_id        = cp.claim_item_id
     AND p.promotion_id = rcd.promotion_id  --12/04/2012
     AND p.promotion_status = NVL(p_in_promo_status,p.promotion_status)  --12/04/2012
     AND ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND p.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))
     AND rcd.submitter_user_id   = p_in_submitter_id
     AND ci.approval_status_type = NVL(p_in_approval_status_type,ci.approval_status_type)
     AND submitter_pax_status    = NVL(p_in_pax_status,submitter_pax_status)
     AND rcd.claim_status =
                                NVL (p_in_claim_status, rcd.claim_status)    
     AND ((p_in_job_code IS NULL) OR (p_in_job_code IS NOT NULL AND submitter_job_position = p_in_job_code))
     AND ((p_in_department IS NULL) OR (p_in_department is NOT NULL AND submitter_department IN (SELECT * FROM TABLE(get_array_varchar(p_in_department)))))
     AND TRUNC(NVL(date_submitted,SYSDATE)) BETWEEN p_in_from_date AND p_in_to_date;

  RETURN v_item_cnt;
  
EXCEPTION
   WHEN OTHERS THEN
     prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                            'Claim Promo: '||p_in_promotion_id||' Sugmitter: '||p_in_submitter_id||'  '||
                            '  Pax Status: '||p_in_pax_status||'  Job Code: '||p_in_job_code||
                            '  Department: '||p_in_department||' --> '||SQLERRM,null);
       v_item_cnt := 0;
       RETURN v_item_cnt;
END;    --fnc_submitter_item_count

/***** Arun S  09/10/2012, Commented out complete delete and insert method
PROCEDURE prc_claim_detail
IS
--
--
-- Purpose: Populate rpt_claim_detail and rpt_claim_product table.
--
-- MODIFICATION HISTORY
-- Person      Date       Comments
-- ---------   ------     ------------------------------------------
-- Bethke      11/09/2005 Initial code.
-- D Murray    12/15/2005 Added nvl to all queries for promotion_id, award_type,
--                        claim_status, pax_status, job_position, department,
--                        product_category_id, and product_subcategory_id
--                        - can not have null for Jasper report parameters.
-- Rachel R    01/13/2006  Added refresh date logic for detail.
-- Raju N      01/24/2007  Bug#14934  Added the logic for proxy userID and name
-- Arun S      01/04/2011  Bug # 35875 Fix, added criteria to look only submitter
--                         transaction_amt from journal to show Points for
--                         selected pax in Participant Activity report instead of
--                         showing team Points amount 
-- Arun S      02/21/2011  Bug # 35014 Fix, For product claim promotion populated Points
--                         to Claims submitted detail report when the quantity submitted 
--                         meets minimum qualifier amount
-- nagarajs    03/21/2012  Bug #40345 fix to generate Claims report for live and expired promotions
--                          but not for deleted promotion. 
--Loganathan   03/11/2019  Bug 78631 - PKG_REPORTs without logs or return code
--------------------------------------------------------------------------------

  c_created_by  CONSTANT RPT_CLAIM_DETAIL.created_by%TYPE:=0;
  v_stage       VARCHAR2(250);
  v_commit_cnt  INTEGER;
  v_get_uname   varchar2(100);

  v_claim_form_step_element_id CLAIM_FORM_STEP_ELEMENT.claim_form_step_element_id%TYPE;

  CURSOR cur_claim IS
    SELECT c.*
    FROM   CLAIM c,
           promotion p                              --03/21/2012
    WHERE c.promotion_id = p.promotion_id           --03/21/2012
      AND p.promotion_status IN ('live','expired')  --03/21/2012
      AND p.is_deleted = 0;                         --03/21/2012

  CURSOR cur_prod(p_claim_id VARCHAR2) IS
    SELECT cp.claim_item_id,product_id, product_qty,serial_id, cia.approver_user_id
    FROM CLAIM_PRODUCT cp, CLAIM_ITEM ci, 
        (SELECT di.claim_item_id, 
            decode(di.APPROVAL_STATUS_TYPE,'approv',dia.APPROVER_USER_ID,null) AS approver_user_id
          FROM CLAIM_ITEM_APPROVER dia, CLAIM_ITEM di
          WHERE  di.claim_item_id = dia.claim_item_id
          AND    di.claim_id = p_claim_id
          AND    dia.date_created = (select max(a.date_created) from claim_item_approver a,
                                                                    claim_item b
                                                                where a.claim_item_id = b.claim_item_id
                                                                and b.claim_id = p_claim_id) ) cia                     
    WHERE  cp.claim_item_id = ci.claim_item_id
    AND    ci.claim_id = p_claim_id
    AND    ci.claim_item_id = cia.claim_item_id (+);
  
  CURSOR cur_node(p_user_id VARCHAR2) IS
    SELECT node_id
    FROM   USER_NODE
    WHERE  user_id = p_user_id
    AND    node_id IN (SELECT node_id
                       FROM RPT_HIERARCHY a);

  rec_rpt_dtl                   RPT_CLAIM_DETAIL%ROWTYPE;
  rec_rpt_prod                  RPT_CLAIM_PRODUCT%ROWTYPE;
  v_parent_category_id          PRODUCT_CATEGORY.parent_category_id%TYPE;
  v_parent_category_name        PRODUCT_CATEGORY.product_category_name%TYPE;
  v_promo_payout_group_id       promo_payout_group.promo_payout_group_id%TYPE;
  v_min_qualifier_met           min_qualifier_status.min_qualifier_met%TYPE;
  v_submitter_country_asset_code rpt_claim_detail.submitter_country_asset_code%TYPE;
  
BEGIN

  DELETE  RPT_CLAIM_PRODUCT;
  DELETE  RPT_CLAIM_DETAIL;
  
    MERGE INTO RPT_REFRESH_DATE B
        USING (
            SELECT 'claimssubmitted' AS report_name, 'details' AS report_type, SYSDATE AS refresh_date
            FROM dual) E
        ON (B.report_name = e.report_name AND b.report_type = e.report_type)
        WHEN MATCHED THEN
            UPDATE SET B.refresh_date = e.refresh_date
        WHEN NOT MATCHED THEN
            INSERT (B.report_name, b.report_type, b.refresh_date)
            VALUES (E.report_name, E.report_type, e.refresh_date) ;

  FOR rec_claim IN cur_claim LOOP
    rec_rpt_dtl := NULL;
    v_get_uname:=null;
    
    v_stage := 'find promotion';
    BEGIN
      SELECT promotion_name,
             promotion_type,
             award_type
      INTO   rec_rpt_dtl.promotion_name,
             rec_rpt_dtl.promotion_type,
             rec_rpt_dtl.award_type
      FROM   PROMOTION
      WHERE  promotion_id = rec_claim.promotion_id;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        NULL;
    END;

    IF rec_rpt_dtl.promotion_type = 'product_claim' THEN

      BEGIN     --02/21/2011  Bug # 35014 Fix
        SELECT COUNT(min_qualifier_met)
          INTO v_min_qualifier_met
          FROM promo_payout_group ppg,
               min_qualifier_status mqs
         WHERE ppg.promo_payout_group_id = mqs.promotion_payout_group_id
           AND promotion_id = rec_claim.promotion_id
           AND submitter_id = rec_claim.submitter_id
           AND min_qualifier_met = 1;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          v_min_qualifier_met := 1;   
      END;

      IF v_min_qualifier_met <> 0 THEN   --02/21/2011  Bug # 35014 Fix

        v_stage := 'sum journal trans amt';
        SELECT SUM(transaction_amt)
          INTO rec_rpt_dtl.award_amount
          FROM JOURNAL
         WHERE user_id = rec_claim.submitter_id   --01/04/2011    Bug # 35875 Fix
           AND journal_id IN ( SELECT DISTINCT( journal_id )
                                 FROM ACTIVITY_JOURNAL, ACTIVITY
                                WHERE ACTIVITY.claim_id = rec_claim.claim_id
                                  AND ACTIVITY_JOURNAL.activity_id = ACTIVITY.activity_id );

        v_stage := 'find submitter app_user';
        BEGIN
          SELECT last_name||', '||first_name||' '||middle_name,
                 last_name,
                 first_name,
                 middle_name,
                 DECODE(is_active,1,'active','inactive') pax_status
          INTO   rec_rpt_dtl.submitter_name,
                 rec_rpt_dtl.submitter_last_name,
                 rec_rpt_dtl.submitter_first_name,
                 rec_rpt_dtl.submitter_middle_name,
                 rec_rpt_dtl.submitter_pax_status
          FROM   APPLICATION_USER
          WHERE  user_id = rec_claim.submitter_id;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
          NULL;
        END;

        v_stage := 'find submitter country asset code';
        BEGIN
          SELECT c.cm_asset_code
            INTO v_submitter_country_asset_code
            FROM user_address ua,
                 country c
           WHERE ua.country_id = c.country_id
             AND ua.user_id    = rec_claim.submitter_id
             AND ua.is_primary = 1;
        EXCEPTION
          WHEN OTHERS THEN
            v_submitter_country_asset_code := NULL;
        END;

        v_stage := 'find submitter pax_emp';
        BEGIN
          SELECT position_type,
                 department_type
          INTO   rec_rpt_dtl.submitter_job_position,
                 rec_rpt_dtl.submitter_department
          FROM   PARTICIPANT_EMPLOYER
          WHERE  user_id = rec_claim.submitter_id
          AND    termination_date IS NULL
          AND    ROWNUM = 1;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
          NULL;
        END;

        v_stage := 'find node';
        BEGIN
          SELECT name
          INTO   rec_rpt_dtl.node_name
          FROM   NODE
          WHERE  node_id = rec_claim.node_id;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
          NULL;
        END;

        v_stage := 'find block_id 1';
        BEGIN
          SELECT a.VALUE
          INTO   rec_rpt_dtl.claim_company_name
          FROM   CLAIM_CFSE a, CLAIM_FORM_STEP_ELEMENT b
          WHERE  a.claim_form_step_element_id = b.claim_form_step_element_id
          AND    b.customer_information_block_id = 1
          AND ROWNUM = 1 --remove after good data is created
          AND    a.claim_id = rec_claim.claim_id;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
          NULL;
        END;

        v_stage := 'find block_id 2';
        BEGIN
          SELECT a.VALUE
          INTO   rec_rpt_dtl.claim_first_name
          FROM   CLAIM_CFSE a, CLAIM_FORM_STEP_ELEMENT b
          WHERE  a.claim_form_step_element_id = b.claim_form_step_element_id
          AND    b.customer_information_block_id = 2
          AND ROWNUM = 1 --remove after good data is created
          AND    a.claim_id = rec_claim.claim_id;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
          NULL;
        END;

        v_stage := 'find block_id 3';
        BEGIN
          SELECT a.VALUE
          INTO   rec_rpt_dtl.claim_last_name
          FROM   CLAIM_CFSE a, CLAIM_FORM_STEP_ELEMENT b
          WHERE  a.claim_form_step_element_id = b.claim_form_step_element_id
          AND    b.customer_information_block_id = 3
          AND ROWNUM = 1 --remove after good data is created
          AND    a.claim_id = rec_claim.claim_id;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
          NULL;
        END;

        v_stage := 'find block_id 4';
        BEGIN
          SELECT a.VALUE
          INTO   rec_rpt_dtl.claim_addr1
          FROM   CLAIM_CFSE a, CLAIM_FORM_STEP_ELEMENT b
          WHERE  a.claim_form_step_element_id = b.claim_form_step_element_id
          AND    b.customer_information_block_id = 4
          AND ROWNUM = 1 --remove after good data is created
          AND    a.claim_id = rec_claim.claim_id;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
          NULL;
        END;

        v_stage := 'find block_id 5';
        BEGIN
          SELECT a.VALUE
          INTO   rec_rpt_dtl.claim_addr2
          FROM   CLAIM_CFSE a, CLAIM_FORM_STEP_ELEMENT b
          WHERE  a.claim_form_step_element_id = b.claim_form_step_element_id
          AND    b.customer_information_block_id = 5
          AND ROWNUM = 1 --remove after good data is created
          AND    a.claim_id = rec_claim.claim_id;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
          NULL;
        END;

        v_stage := 'find block_id 6';
        BEGIN
          SELECT a.VALUE
          INTO   rec_rpt_dtl.claim_phone1
          FROM   CLAIM_CFSE a, CLAIM_FORM_STEP_ELEMENT b
          WHERE  a.claim_form_step_element_id = b.claim_form_step_element_id
          AND    b.customer_information_block_id = 6
          AND ROWNUM = 1 --remove after good data is created
          AND    a.claim_id = rec_claim.claim_id;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
          NULL;
        END;

        v_stage := 'find block_id 7';
        BEGIN
          SELECT a.VALUE
          INTO   rec_rpt_dtl.claim_phone2
          FROM   CLAIM_CFSE a, CLAIM_FORM_STEP_ELEMENT b
          WHERE  a.claim_form_step_element_id = b.claim_form_step_element_id
          AND    b.customer_information_block_id = 7
          AND ROWNUM = 1 --remove after good data is created
          AND    a.claim_id = rec_claim.claim_id;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
          NULL;
        END;

        v_stage := 'find block_id 8';
        BEGIN
          SELECT a.VALUE
          INTO   rec_rpt_dtl.claim_email1
          FROM   CLAIM_CFSE a, CLAIM_FORM_STEP_ELEMENT b
          WHERE  a.claim_form_step_element_id = b.claim_form_step_element_id
          AND    b.customer_information_block_id = 8
          AND ROWNUM = 1 --remove after good data is created
          AND    a.claim_id = rec_claim.claim_id;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
          NULL;
        END;

        v_stage := 'find block_id 9';
        BEGIN
          SELECT a.VALUE
          INTO   rec_rpt_dtl.claim_email2
          FROM   CLAIM_CFSE a, CLAIM_FORM_STEP_ELEMENT b
          WHERE  a.claim_form_step_element_id = b.claim_form_step_element_id
          AND    b.customer_information_block_id = 9
          AND ROWNUM = 1 --remove after good data is created
          AND    a.claim_id = rec_claim.claim_id;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
          NULL;
        END;
      
        If rec_claim.proxy_user_id is not null then
        v_get_uname:=FNC_GET_USER_NAME(rec_claim.proxy_user_id);
        Else
        v_get_uname:=null;
        End if;

        v_stage := 'insert rpt_claim_detail';
        INSERT INTO RPT_CLAIM_DETAIL
              (rpt_claim_detail_id,
               claim_id,
               claim_number,
               claim_status,
               date_submitted,
               promotion_id,
               promotion_name,
               promotion_type,
               award_type,
               sub_promotion_id,
               sub_promotion_name,
               sub_promotion_type,
               award_amount,
               proxy_user_id,
               proxy_user_name,
               submitter_user_id,
               submitter_name,
               submitter_last_name,
               submitter_first_name,
               submitter_middle_name,
               submitter_pax_status,
               submitter_job_position,
               submitter_department,
               node_id,
               node_name,
               claim_company_name,
               claim_first_name,
               claim_last_name,
               claim_addr1,
               claim_addr2,
               claim_phone1,
               claim_phone2,
               claim_email1,
               claim_email2,
               date_created,
               created_by,
               submitter_country_asset_code)
        VALUES(rpt_claim_detail_pk_sq.NEXTVAL,
               rec_claim.claim_id,
               rec_claim.claim_number,
--             nvl(to_char(rec_claim.is_open),' '), -- claim_status
               NVL(DECODE(rec_claim.is_open,1,'open',0,'closed'),' '), -- claim_status
               TRUNC(rec_claim.submission_date), --date_submitted

               NVL(rec_claim.promotion_id,0),
               rec_rpt_dtl.promotion_name,
               rec_rpt_dtl.promotion_type,
               NVL(rec_rpt_dtl.award_type,' '),

               NULL, --sub_promotion_id,
               NULL, --sub_promotion_name,
               NULL, --sub_promotion_type,

               rec_rpt_dtl.award_amount, --award_amount,
               rec_claim.proxy_user_id,
               DECODE(rec_claim.proxy_user_id,'','',FNC_GET_USER_NAME(rec_claim.proxy_user_id)),  --Bug # 30618 Fix --v_get_uname,--FNC_GET_USER_NAME(rec_claim.proxy_user_id)

               rec_claim.submitter_id,
               rec_rpt_dtl.submitter_name,
               rec_rpt_dtl.submitter_last_name,
               rec_rpt_dtl.submitter_first_name,
               rec_rpt_dtl.submitter_middle_name,
               NVL(rec_rpt_dtl.submitter_pax_status,' '),
               NVL(rec_rpt_dtl.submitter_job_position,' '),
               NVL(rec_rpt_dtl.submitter_department,' '),

               rec_claim.node_id,
               rec_rpt_dtl.node_name,

               rec_rpt_dtl.claim_company_name,
               rec_rpt_dtl.claim_first_name,
               rec_rpt_dtl.claim_last_name,
               rec_rpt_dtl.claim_addr1,
               rec_rpt_dtl.claim_addr2,
               rec_rpt_dtl.claim_phone1,
               rec_rpt_dtl.claim_phone2,
               rec_rpt_dtl.claim_email1,
               rec_rpt_dtl.claim_email2,

               SYSDATE,
               c_created_by,
               v_submitter_country_asset_code);

        v_commit_cnt := v_commit_cnt + SQL%ROWCOUNT;

        FOR rec_prod IN cur_prod(rec_claim.claim_id) LOOP

          rec_rpt_prod := NULL;

          v_stage := 'find product';
          BEGIN
            SELECT product_name,
                   category_id
            INTO   rec_rpt_prod.product_name,
                   rec_rpt_prod.product_subcategory_id
            FROM   PRODUCT
            WHERE  product_id = rec_prod.product_id;
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
            NULL;
          END;

          BEGIN
            SELECT SUM(j.transaction_amt)
            INTO   rec_rpt_prod.product_award_amount
            FROM   ACTIVITY a, ACTIVITY_JOURNAL aj, JOURNAL j
            WHERE  a.activity_id = aj.activity_id
            AND    aj.journal_id = j.journal_id
            AND    a.claim_id = rec_claim.claim_id
            AND    a.product_id = rec_prod.product_id;
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
             NULL;
          END;

          SELECT parent_category_id, product_category_name
            INTO v_parent_category_id, v_parent_category_name
            FROM PRODUCT_CATEGORY
           WHERE product_category_id = rec_rpt_prod.product_subcategory_id;


         IF v_parent_category_id IS NULL THEN
            rec_rpt_prod.product_category_id := rec_rpt_prod.product_subcategory_id;
            rec_rpt_prod.product_category_name := v_parent_category_name;
            rec_rpt_prod.product_subcategory_name := NULL;
            rec_rpt_prod.product_subcategory_id := NULL;
         ELSE

           v_stage := 'find product_sub_category';
           BEGIN
            SELECT product_category_name,
                   parent_category_id
            INTO   rec_rpt_prod.product_subcategory_name,
                   rec_rpt_prod.product_category_id
            FROM   PRODUCT_CATEGORY
            WHERE  product_category_id = rec_rpt_prod.product_subcategory_id;
           EXCEPTION
            WHEN NO_DATA_FOUND THEN
            NULL;
           END;

           v_stage := 'find product_category';
           BEGIN
            SELECT product_category_name
            INTO   rec_rpt_prod.product_category_name
            FROM   PRODUCT_CATEGORY
            WHERE  product_category_id = rec_rpt_prod.product_category_id;
           EXCEPTION
            WHEN NO_DATA_FOUND THEN
            NULL;
           END;
         END IF;

          IF rec_claim.is_open = 0 THEN
        
            IF rec_prod.approver_user_id IS NOT NULL THEN
                rec_rpt_prod.approver_user_id := rec_prod.approver_user_id;
                   rec_rpt_prod.approver_name := fnc_get_final_appr_name(rec_prod.approver_user_id);                   
            ELSE
                rec_prod.approver_user_id := NULL;
                rec_rpt_prod.approver_name := NULL;
            END IF;
          END IF;
  
          v_stage := 'insert rpt_claim_product';
          INSERT INTO RPT_CLAIM_PRODUCT
                (rpt_claim_product_id,
                 rpt_claim_detail_id,
                 claim_product_id,
                 claim_id,
                 product_id,
                 product_name,
                 product_qty,
                 product_category_id,
                 product_category_name,
                 product_subcategory_id,
                 product_subcategory_name,
                 date_approved,
                 approver_user_id,
                 approver_name,
                 approver_first_name,
                 approver_last_name,
                 date_created,
                 created_by,
                 product_award_amount)
          VALUES(rpt_claim_product_pk_sq.NEXTVAL,
                 rpt_claim_detail_pk_sq.NEXTVAL,
                 rec_prod.claim_item_id,
                 rec_claim.claim_id,
                 rec_prod.product_id,
                 rec_rpt_prod.product_name,
                 rec_prod.product_qty,
                 NVL(rec_rpt_prod.product_category_id,0),
                 rec_rpt_prod.product_category_name,
                 NVL(rec_rpt_prod.product_subcategory_id,0),
                 rec_rpt_prod.product_subcategory_name,
                 NULL, --date_approved,
                 rec_rpt_prod.approver_user_id, --approver_user_id,
                 rec_rpt_prod.approver_name, --approver_name,
                 NULL, --approver_first_name,
                 NULL, --approver_last_name,
                 SYSDATE,
                 c_created_by,
                 rec_rpt_prod.product_award_amount);

          v_commit_cnt := v_commit_cnt + SQL%ROWCOUNT;
  
        END LOOP; --rec_prod
      END IF;   --IF v_min_qualifier_met = 1 THEN
    END IF; --rec_rpt_dtl.promotion_type = 'product_claim'

    IF v_commit_cnt > 500 THEN

      v_commit_cnt := 0;
    END IF;

  END LOOP; --rec_pax


EXCEPTION
  WHEN OTHERS THEN
    prc_execution_log_entry('prc_claim_detail',1,'ERROR',v_stage||' '||SQLERRM,null);
END;
**** Arun S  09/10/2012, Commented out complete delete and insert method*/


PROCEDURE prc_claim_detail
  (p_user_id              IN NUMBER,
   p_start_date           IN DATE, 
   p_end_date             IN DATE, 
   p_return_code          OUT NUMBER,
   p_error_message        OUT VARCHAR2
   )
  IS
  
/*-----------------------------------------------------------------------------
--
-- Purpose: Populate rpt_claim_detail and rpt_claim_product table.
--
-- MODIFICATION HISTORY
-- Person      Date       Comments
-- ---------   ------     ------------------------------------------
-- Bethke          11/09/2005  Initial code.
-- D Murray        12/15/2005  Added nvl to all queries for promotion_id, award_type,
--                              claim_status, pax_status, job_position, department,
--                              product_category_id, and product_subcategory_id
--                              - can not have null for Jasper report parameters.
-- Rachel R        01/13/2006  Added refresh date logic for detail.
-- Raju N          01/24/2007  Bug#14934  Added the logic for proxy userID and name
-- Arun S          01/04/2011  Bug # 35875 Fix, added criteria to look only submitter
--                              transaction_amt from journal to show Points for
--                              selected pax in Participant Activity report instead of
--                              showing team Points amount 
-- Arun S          02/21/2011  Bug # 35014 Fix, For product claim promotion populated Points
--                              to Claims submitted detail report when the quantity submitted 
--                              meets minimum qualifier amount
-- nagarajs        03/21/2012  Bug #40345 fix to generate Claims report for live and expired promotions
--                              but not for deleted promotion. 
-- Arun S          09/10/2012  G5 report changes
                                Modified process with incremental approach based on 
                                input p_start_date and p_end_date
                                Removed populating records to rpt_claim_product, another
                                process pkg_report_product_claim.prc_claim_detail_for_prd_claim used for
                                populating records to rpt_claim_product
                                When submitter information updated then made to update/insert
                                in rpt_claim_summary in this process itself      
--Ravi Dhanekula   06/26/2012  Bug # 41603 To include open claims in the report.
                   11/14/2012  Bug # 44074 Changed declaration for v_min_qualifier_met to accept value more than 9.
                   10/07/2013  Change required to include merch level sweepstakes.
                   01/14/2014  Fixed the bugs # 50999 and 51001
--Swati            06/02/2014  Bug 51044 - Reverse transactions are not accounted for in reporting    
--nagarajs         10/06/2014  Bug 57241 Fix - Sweeptake count and sweeptake points not showing in report when claim staus selected as 'closed'
--kiran kumar      04/08/2015  Bug 61147 Fix - pkg_report_claim.prc_claim_detail raising exact fetch returns more than requested number of rows
                                For PARTICIPANT_EMPLOYER either job_position or department should present.
                                As Department is populating as null while inserting into rpt_claim_summary. check condition was failing, resulting duplicate records were populated.
                                Handled nvl condition for department_type, position_type, DEPARTMENT, JOB_POSITION
--Suriya N         05/28/2015  Bug 62440 - Claim Activity report not showing data if user doesn't have any active employment details                                
--Suresh J         08/07/2015  Bug 63594 - Reports - Claims Activity - List of Participants - Points are not displayed for Product claim with Stack Rank                               
--nagarajs         01/04/2016  Bug 64949 - Reporting showing multiple points awarded for a a tiered product claim promotion
-----------------------------------------------------------------------------*/


  CURSOR cur_claim IS
    SELECT c.claim_id,
           c.submitter_id,
           c.node_id,
           c.proxy_user_id,
           c.claim_group_id,
           c.is_open,
           c.approval_round,
           c.last_approval_node_id,
           c.claim_number,
           c.approver_comments,
           c.submission_date,
           c.promotion_id,
           p.promotion_name,
           p.promotion_type,
           p.award_type,
           fnc_get_badge_count_by_user(c.submitter_id,'product_claim',trunc(c.submission_date)) AS badges_earned
           ,sr.stack_rank_id   --08/07/2015
      FROM claim c,
           promotion p
           ,stack_rank sr      --08/07/2015     
     WHERE c.promotion_id     = p.promotion_id
       AND p.promotion_status IN ('live','expired')
       AND p.is_deleted       = 0       
       AND p.promotion_type   = 'product_claim'
       AND p.promotion_id = sr.promotion_id (+)  --08/07/2015        
       AND (p_start_date      < c.date_created  OR p_start_date < c.date_modified)
       AND (c.date_created    <= p_end_date     OR c.date_modified <= p_end_date);

CURSOR cur_sweep_activity  --(in_activity_id IN NUMBER)  --Chidamba 06/29/2012 alter cursor
     IS     
      SELECT
        un.node_id as node_id,
        a.activity_id, 
        a.date_created as award_date,
        n.name as node_name,
        p.promotion_name as promotion_name,
        nvl(p.promotion_id,0) as promotion_id,
        nvl(p.award_type,' ') as award_type,
        p.is_taxable as is_taxable,
        a.quantity as points,
        ua.country_id as country_id,
        au.user_id as participant_id,
        au.first_name as participant_first_name,
        au.middle_name as participant_middle_name,
        au.last_name as participant_last_name,
                nvl(DECODE(au.is_active,1,'active',0,'inactive'),' ') participant_status,
        nvl(pe.position_type,' ') as job_title,
        nvl(pe.department_type, ' ') as department,
        DECODE (a.activity_discrim,'sweep',1,'sweeplevel',1,0) AS sweepstakes_won --10/07/2013
     FROM activity a,
          node n,
          promotion p,
          promo_product_claim pq,
          application_user au,
          user_node un,
          user_address ua,
          participant pax,
          participant_employer pe
      WHERE 
         a.activity_discrim IN ('sweep','sweeplevel') --10/07/2013
        AND a.user_id =un.user_id
        AND a.user_id =au.user_id
        AND un.is_primary =1 
        AND un.node_id = n.node_id
        AND a.promotion_id = p.promotion_id
        AND p.promotion_id = pq.promotion_id
        AND au.user_id = pax.user_id
        AND au.user_id = ua.user_id
        AND pax.user_id = pe.user_id(+)
        AND pe.termination_date IS NULL
        AND p.promotion_status IN ('live','expired')            
        AND p.is_deleted = 0      
        AND (p_start_date   <  a.date_created   AND a.date_created <= p_end_date);

  --Cursor to pick the Submitter users whose information is updated in
  --application_user,participant_employer table
  CURSOR cur_sub_info_changed IS
    SELECT au.user_id,
           au.last_name,
           au.first_name,
           au.middle_name,
           au.suffix,
           DECODE(au.is_active,1,'active','inactive') pax_status,
           NVL(vcpe.department_type,' ') department_type,     --Bug 61147
           NVL(vcpe.position_type, ' ') position_type,        --Bug 61147
           rpt.award_type,
           TRUNC(rpt.date_submitted) date_submitted,
           COUNT(rpt.claim_id) claim_count,
           SUM(rpt.award_amount) award_amount,
           rpt.claim_status,
           rpt.node_id,
           rpt.promotion_id,
           rpt.submitter_pax_status     pax_status_old,
           rpt.submitter_department     department_old,
           rpt.submitter_job_position   job_position_old,          
           rpt.submitter_sweepstakes_won,
--           fnc_get_badge_count_by_user(au.user_id,'product_claim') submitter_badges_earned_new,
           rpt.submitter_badges_earned         
      FROM application_user au,
           rpt_claim_detail rpt,
           vw_curr_pax_employer vcpe
     WHERE au.user_id   = rpt.submitter_user_id
       AND vcpe.user_id = rpt.submitter_user_id
       AND (p_start_date < au.date_created AND au.date_created <= p_end_date
            OR p_start_date < au.date_modified  AND  au.date_modified <= p_end_date)
       AND (au.last_name            <> rpt.submitter_last_name
            OR au.first_name        <> rpt.submitter_first_name
            OR au.middle_name       <> rpt.submitter_middle_name
            OR DECODE(au.is_active,1,'active','inactive') <> rpt.submitter_pax_status
            OR NVL(vcpe.department_type,' ') <> rpt.submitter_department              --Bug 61147
            OR NVL(vcpe.position_type,' ')   <> rpt.submitter_job_position)           --Bug 61147
     GROUP BY au.user_id,au.last_name,au.first_name,au.middle_name,
              au.suffix,DECODE(au.is_active,1,'active','inactive'),
              NVL(vcpe.department_type,' '),NVL(vcpe.position_type,' '),rpt.award_type,    --Bug 61147
              TRUNC(rpt.date_submitted),rpt.claim_status,
              rpt.node_id,rpt.promotion_id,rpt.submitter_pax_status,
              rpt.submitter_department,rpt.submitter_job_position,
              rpt.submitter_sweepstakes_won,
              rpt.submitter_badges_earned
     ORDER BY date_submitted, promotion_id, claim_status,node_id;   

  --Cursor hierarchy
  CURSOR cur_hier (p_in_node_id rpt_hierarchy.node_id%TYPE) IS
    SELECT parent_node_id header_node_id,
           node_id,
           hier_level,
           is_leaf
      FROM rpt_hierarchy
     START WITH node_id = p_in_node_id
   CONNECT BY PRIOR parent_node_id = node_id;       

  --Cursor to pick modified node name
  CURSOR cur_node_changed IS
    SELECT node_id,
           name
      FROM node
     WHERE date_modified BETWEEN p_start_date AND p_end_date;  
     
  CURSOR cur_reversed (v_start_date IN DATE, v_end_date IN DATE)  --06/02/2014 Bug # 51044
  IS
  SELECT rpt.*
  FROM rpt_claim_detail rpt, activity_journal aj, activity a,journal j
 WHERE     rpt.claim_id = a.claim_id
       AND a.activity_id = aj.activity_id
       AND j.is_reverse = 1 
       AND J.journal_id = aJ.journal_id
       AND (v_start_date   <  J.date_modified   AND J.date_modified <= v_end_date);--10/8/2014 Bug # 51044

              
  --Execution log variables
  v_process_name            execution_log.process_name%type  := 'prc_claim_detail';
  v_release_level           execution_log.release_level%type := '1';

  --Procedure variables
  v_param                   VARCHAR2(250);
  v_stage                   VARCHAR2(250);
  v_insert_rpt              VARCHAR2(1) := 'N';
  rec_rpt_dtl               rpt_claim_detail%ROWTYPE;
  v_claim_status_old        rpt_claim_detail.claim_status%TYPE;
  v_min_qualifier_met       NUMBER(18);--min_qualifier_status.min_qualifier_met%TYPE; --11/14/2012
  v_rpt_claim_detail_id     rpt_claim_detail.rpt_claim_detail_id%TYPE;
  v_tab_node_id             dbms_sql.number_table;
  v_tab_user_id             dbms_sql.number_table;
  v_tab_node_name           dbms_sql.varchar2_table;
  v_header_node_id          rpt_hierarchy.parent_node_id%TYPE;
  v_node_id                 rpt_hierarchy.node_id%TYPE;
  v_hier_level              rpt_hierarchy.hier_level%TYPE;
  v_is_leaf                 rpt_hierarchy.is_leaf%TYPE;
  v_summ_id                 rpt_claim_summary.rpt_claim_summary_id%TYPE;
  v_claim_cnt               NUMBER := 0;
  
           
BEGIN

  v_stage := 'Procedure Started';
  
  v_param := 'Start Date:'||TO_CHAR(p_start_date,'MM/DD/YYYY HH:MI:SS AM')||
             ' End Date:'||TO_CHAR(p_end_date,'MM/DD/YYYY HH:MI:SS AM');

  prc_execution_log_entry(v_process_name, v_release_level, 'INFO', 
                          v_stage||' for Input '||v_param, NULL);
 
FOR rec_sweep_activity IN cur_sweep_activity
    LOOP
    INSERT INTO rpt_claim_detail
              (rpt_claim_detail_id,
               claim_id,
               claim_number,
               claim_status,
               date_submitted,
               promotion_id,
               promotion_name,
               promotion_type,
               award_type,
               sub_promotion_id,
               sub_promotion_name,
               sub_promotion_type,
               award_amount,
               proxy_user_id,
               proxy_user_name,
               submitter_user_id,
               submitter_name,
               submitter_last_name,
               submitter_first_name,
               submitter_middle_name,
               submitter_pax_status,
               submitter_job_position,
               submitter_department,
               node_id,
               node_name,
               claim_company_name,
               claim_first_name,
               claim_last_name,
               claim_addr1,
               claim_addr2,
               claim_phone1,
               claim_phone2,
               claim_email1,
               claim_email2,
               date_created,
               created_by,
               submitter_country_id,
               submitter_points,               
               submitter_sweepstakes_won,
               submitter_badges_earned,
               version
               )
        VALUES(rpt_claim_detail_pk_sq.NEXTVAL,
               NULL,--rec_sweep_activity.claim_id,
               NULL,--rec_claim.claim_number,
               'closed', --NULL,--NVL(DECODE(rec_claim.is_open,1,'open',0,'closed'),' '), -- claim_status --10/06/2014 Bug 57241 Fix
               rec_sweep_activity.award_date,   --TRUNC(rec_claim.submission_date), --date_submitted

               NVL(rec_sweep_activity.promotion_id,0),
               rec_sweep_activity.promotion_name,
               'product_claim',
               NVL(rec_sweep_activity.award_type,' '),

               NULL, --sub_promotion_id,
               NULL, --sub_promotion_name,
               NULL, --sub_promotion_type,
               rec_sweep_activity.points, --award_amount,
               NULL,
               NULL,
               rec_sweep_activity.participant_id,
               rec_sweep_activity.participant_last_name||' , '||rec_sweep_activity.participant_first_name,
               rec_sweep_activity.participant_last_name,
               rec_sweep_activity.participant_first_name,
               rec_sweep_activity.participant_middle_name,
               NVL(rec_sweep_activity.participant_status,' '),
               NVL(rec_sweep_activity.job_title,' '),
               NVL(rec_sweep_activity.department,' '),
                           rec_sweep_activity.node_id,
               rec_sweep_activity.node_name,
               NULL,--rec_rpt_dtl.claim_company_name,
               NULL,--rec_rpt_dtl.claim_first_name,
               NULL,--rec_rpt_dtl.claim_last_name,
               NULL,--rec_rpt_dtl.claim_addr1,
               NULL,--rec_rpt_dtl.claim_addr2,
               NULL,--rec_rpt_dtl.claim_phone1,
               NULL,--rec_rpt_dtl.claim_phone2,
               NULL,--rec_rpt_dtl.claim_email1,
               NULL,--rec_rpt_dtl.claim_email2,

               SYSDATE,
               p_user_id,
               rec_sweep_activity.country_id,             
               NULL,
               rec_sweep_activity.sweepstakes_won,
               NULL, --10/25/2012
               1
               );
    
    END LOOP;
  
  FOR rec_claim IN cur_claim LOOP

    rec_rpt_dtl := NULL;
    v_claim_status_old := NULL;

    v_stage := 'find submitter app_user info';
    BEGIN
      SELECT au.last_name||' , '||au.first_name, --01/14/2014
             au.last_name,
             au.first_name,
             au.middle_name,
             DECODE(au.is_active,1,'active','inactive') pax_status,
             NVL(vcpe.position_type, ' ') position_type,          --Bug 61147
             NVL(vcpe.department_type,' ') department_type        --Bug 61147
           --  fnc_get_badge_count_by_user(au.user_id,'product_claim')
        INTO rec_rpt_dtl.submitter_name,
             rec_rpt_dtl.submitter_last_name,
             rec_rpt_dtl.submitter_first_name,
             rec_rpt_dtl.submitter_middle_name,
             rec_rpt_dtl.submitter_pax_status,
             rec_rpt_dtl.submitter_job_position,
             rec_rpt_dtl.submitter_department
           --  rec_rpt_dtl.submitter_badges_earned
        FROM application_user au,
             vw_curr_pax_employer vcpe
       WHERE au.user_id = vcpe.user_id (+)      --05/28/2015
         AND au.user_id = rec_claim.submitter_id; 
                 
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        rec_rpt_dtl.submitter_name        := NULL;
        rec_rpt_dtl.submitter_last_name   := NULL;
        rec_rpt_dtl.submitter_first_name  := NULL;
        rec_rpt_dtl.submitter_middle_name := NULL;
        rec_rpt_dtl.submitter_pax_status  := NULL;
        rec_rpt_dtl.submitter_job_position:= NULL;
        rec_rpt_dtl.submitter_department  := NULL;      
       --  rec_rpt_dtl.submitter_badges_earned  := NULL;    
    END;

    BEGIN     --02/21/2011  Bug # 35014 Fix
      v_stage := 'Check for min qualifier met';
      SELECT COUNT(DECODE(min_qualifier_met,NULL,ppg.promo_payout_group_id,min_qualifier_met)) --01/14/2014  fix for bug # 50999
        INTO v_min_qualifier_met
        FROM promo_payout_group ppg,
             min_qualifier_status mqs
       WHERE ppg.promo_payout_group_id = mqs.promotion_payout_group_id(+)
         AND promotion_id = rec_claim.promotion_id
         AND submitter_id(+) = rec_claim.submitter_id
         AND min_qualifier_met(+) = 1;
                      
    EXCEPTION
      WHEN OTHERS THEN
        v_min_qualifier_met := 0;
    END;

    IF v_min_qualifier_met <> 0 THEN   --02/21/2011  Bug # 35014 Fix
      BEGIN
        v_stage := 'sum journal trans amt';
     IF rec_claim.stack_rank_id IS NULL THEN   --08/07/2015
        SELECT SUM(transaction_amt)
          INTO rec_rpt_dtl.award_amount
          FROM journal
         WHERE user_id = rec_claim.submitter_id     --01/04/2011    Bug # 35875 Fix
           AND journal_id IN ( SELECT DISTINCT( aj.journal_id )
                                 FROM activity_journal aj, 
                                      activity act 
                                WHERE act.claim_id = rec_claim.claim_id
                                  AND aj.activity_id = act.activity_id 
                                  AND NVL(act.is_carryover,0) = 0)   --01/04/2016  
           AND status_type != 'pend_min_qual';                       --01/04/2016  
     ELSE  --08/07/2015
        SELECT SUM(transaction_amt) as points_stack_rank  --08/07/2015
          INTO rec_rpt_dtl.award_amount        
           FROM journal j
          WHERE j.user_id = rec_claim.submitter_id AND
                rec_claim.stack_rank_id IS NOT NULL AND
                (journal_type = 'StackRank' AND j.journal_id IN ( SELECT DISTINCT( aj.journal_id )
                                 FROM activity act,
                                      activity_journal aj,
                                      stack_rank_node srn,
                                      stack_rank sr,
                                      stack_rank_participant srp  
                                WHERE (act.stack_rank_participant_id = srp.stack_rank_participant_id and
                                       srp.stack_rank_node_id = srn.stack_rank_node_id and
                                       srn.stack_rank_id = sr.stack_rank_id and
                                       sr.stack_rank_id = rec_claim.stack_rank_id and
                                       sr.promotion_id = j.promotion_id and
                                       act.activity_id = aj.activity_id and
                                       aj.journal_id = j.journal_id and
                                       act.claim_id is null and
                                       srp.user_id = j.user_id  )));
     END IF;  --08/07/2015                                
      EXCEPTION
        WHEN OTHERS THEN
          rec_rpt_dtl.award_amount := 0;
      END;
    END IF;   --IF v_min_qualifier_met = 1 THEN
   

    v_stage := 'find sweepstakes_won for the claim';            
    BEGIN
      SELECT COUNT(claim_id)
        INTO rec_rpt_dtl.submitter_sweepstakes_won
        FROM claim_item
       WHERE approval_status_type = 'winner' 
         AND claim_id = rec_claim.claim_id;
    EXCEPTION
      WHEN OTHERS THEN
        rec_rpt_dtl.submitter_sweepstakes_won := 0; 
    END;
    
    BEGIN
      v_stage := 'Check claim in report table';
      SELECT rpt_claim_detail_id,
             claim_status
        INTO rec_rpt_dtl.rpt_claim_detail_id,
             v_claim_status_old
        FROM rpt_claim_detail
       WHERE claim_id = rec_claim.claim_id;

      SELECT NVL(DECODE(rec_claim.is_open, 1,'open', 0, 'closed'),' ')
        INTO rec_rpt_dtl.claim_status
        FROM dual;
       
      v_stage := 'Update report table';
      UPDATE rpt_claim_detail
         SET claim_status              = rec_rpt_dtl.claim_status,
             award_amount              = rec_rpt_dtl.award_amount,
             submitter_points          = rec_rpt_dtl.award_amount,
             submitter_sweepstakes_won = rec_rpt_dtl.submitter_sweepstakes_won,
           -- submitter_badges_earned = rec_rpt_dtl.submitter_badges_earned, --10/25/2012
             modified_by               = p_user_id,
             date_modified             = SYSDATE,
             version                   = version + 1
       WHERE rpt_claim_detail_id = rec_rpt_dtl.rpt_claim_detail_id; 

      v_insert_rpt := 'N';
             
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        v_insert_rpt := 'Y';    
    END;
       
    IF v_insert_rpt = 'Y' THEN
      
      v_stage := 'find submitter country id';
      BEGIN
        SELECT ua.country_id
          INTO rec_rpt_dtl.submitter_country_id
          FROM user_address ua
        --       country c
         WHERE --ua.country_id = c.country_id
            ua.user_id    = rec_claim.submitter_id
           AND ua.is_primary = 1;
      EXCEPTION
        WHEN OTHERS THEN
          rec_rpt_dtl.submitter_country_id := NULL;
      END;

      v_stage := 'find node';
      BEGIN
        SELECT name
          INTO rec_rpt_dtl.node_name
          FROM node
         WHERE node_id = rec_claim.node_id;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          rec_rpt_dtl.node_name := NULL;
      END;

      v_stage := 'find block_id 1';
      BEGIN
        SELECT a.VALUE
          INTO rec_rpt_dtl.claim_company_name
          FROM claim_cfse a, 
               claim_form_step_element b
         WHERE a.claim_form_step_element_id = b.claim_form_step_element_id
           AND b.customer_information_block_id = 1
           AND ROWNUM = 1 --remove after good data is created
           AND a.claim_id = rec_claim.claim_id;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          rec_rpt_dtl.claim_company_name := NULL;
      END;

      v_stage := 'find block_id 2';
      BEGIN
        SELECT a.VALUE
          INTO rec_rpt_dtl.claim_first_name
          FROM claim_cfse a, 
               claim_form_step_element b
         WHERE a.claim_form_step_element_id = b.claim_form_step_element_id
           AND b.customer_information_block_id = 2
           AND ROWNUM = 1 --remove after good data is created
           AND a.claim_id = rec_claim.claim_id;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          rec_rpt_dtl.claim_first_name := NULL;
      END;

      v_stage := 'find block_id 3';
      BEGIN
        SELECT a.VALUE
          INTO rec_rpt_dtl.claim_last_name
          FROM claim_cfse a, 
               claim_form_step_element b
         WHERE a.claim_form_step_element_id = b.claim_form_step_element_id
           AND b.customer_information_block_id = 3
           AND ROWNUM = 1 --remove after good data is created
           AND a.claim_id = rec_claim.claim_id;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          rec_rpt_dtl.claim_last_name := NULL;
      END;

      v_stage := 'find block_id 4';
      BEGIN
        SELECT a.VALUE
          INTO rec_rpt_dtl.claim_addr1
          FROM claim_cfse a, 
               claim_form_step_element b
         WHERE a.claim_form_step_element_id = b.claim_form_step_element_id
           AND b.customer_information_block_id = 4
           AND ROWNUM = 1 --remove after good data is created
           AND a.claim_id = rec_claim.claim_id;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          rec_rpt_dtl.claim_addr1 := NULL;
      END;

      v_stage := 'find block_id 5';
      BEGIN
        SELECT a.VALUE
          INTO rec_rpt_dtl.claim_addr2
          FROM claim_cfse a, 
               claim_form_step_element b
         WHERE a.claim_form_step_element_id = b.claim_form_step_element_id
           AND b.customer_information_block_id = 5
           AND ROWNUM = 1 --remove after good data is created
           AND a.claim_id = rec_claim.claim_id;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          rec_rpt_dtl.claim_addr2 := NULL;
      END;

      v_stage := 'find block_id 6';
      BEGIN
        SELECT a.VALUE
          INTO rec_rpt_dtl.claim_phone1
          FROM claim_cfse a, 
               claim_form_step_element b
         WHERE a.claim_form_step_element_id = b.claim_form_step_element_id
           AND b.customer_information_block_id = 6
           AND ROWNUM = 1 --remove after good data is created
           AND a.claim_id = rec_claim.claim_id;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          rec_rpt_dtl.claim_phone1 := NULL;
      END;

      v_stage := 'find block_id 7';
      BEGIN
        SELECT a.VALUE
          INTO rec_rpt_dtl.claim_phone2
          FROM claim_cfse a, 
               claim_form_step_element b
         WHERE a.claim_form_step_element_id = b.claim_form_step_element_id
           AND b.customer_information_block_id = 7
           AND ROWNUM = 1 --remove after good data is created
           AND a.claim_id = rec_claim.claim_id;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          rec_rpt_dtl.claim_phone2 := NULL;
      END;

      v_stage := 'find block_id 8';
      BEGIN
        SELECT a.VALUE
          INTO rec_rpt_dtl.claim_email1
          FROM claim_cfse a, 
               claim_form_step_element b
         WHERE a.claim_form_step_element_id = b.claim_form_step_element_id
           AND b.customer_information_block_id = 8
           AND ROWNUM = 1 --remove after good data is created
           AND a.claim_id = rec_claim.claim_id;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          rec_rpt_dtl.claim_email1 := NULL;
      END;

      v_stage := 'find block_id 9';
      BEGIN
        SELECT a.VALUE
          INTO rec_rpt_dtl.claim_email2
          FROM claim_cfse a, 
               claim_form_step_element b
         WHERE a.claim_form_step_element_id = b.claim_form_step_element_id
           AND b.customer_information_block_id = 9
           AND ROWNUM = 1 --remove after good data is created
           AND a.claim_id = rec_claim.claim_id;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          rec_rpt_dtl.claim_email2 := NULL;
      END;
      
      /*
      IF rec_claim.proxy_user_id IS NOT NULL THEN
        v_get_uname := fnc_get_user_name(rec_claim.proxy_user_id);
      ELSE
        v_get_uname := NULL;
      END IF;
      */
 
      SELECT rpt_claim_detail_pk_sq.NEXTVAL
        INTO v_rpt_claim_detail_id 
        FROM dual;
             
        v_stage := 'Insert rpt_claim_detail';
        INSERT INTO rpt_claim_detail
              (rpt_claim_detail_id,
               claim_id,
               claim_number,
               claim_status,
               date_submitted,
               promotion_id,
               promotion_name,
               promotion_type,
               award_type,
               sub_promotion_id,
               sub_promotion_name,
               sub_promotion_type,
               award_amount,
               proxy_user_id,
               proxy_user_name,
               submitter_user_id,
               submitter_name,
               submitter_last_name,
               submitter_first_name,
               submitter_middle_name,
               submitter_pax_status,
               submitter_job_position,
               submitter_department,
               node_id,
               node_name,
               claim_company_name,
               claim_first_name,
               claim_last_name,
               claim_addr1,
               claim_addr2,
               claim_phone1,
               claim_phone2,
               claim_email1,
               claim_email2,
               date_created,
               created_by,
               submitter_country_id,
               submitter_points,               
               submitter_sweepstakes_won,
               submitter_badges_earned,
               version
               )
        VALUES(v_rpt_claim_detail_id,
               rec_claim.claim_id,
               rec_claim.claim_number,
               NVL(DECODE(rec_claim.is_open,1,'open',0,'closed'),' '), -- claim_status
               rec_claim.submission_date,   --TRUNC(rec_claim.submission_date), --date_submitted

               NVL(rec_claim.promotion_id,0),
               rec_claim.promotion_name,
               rec_claim.promotion_type,
               NVL(rec_claim.award_type,' '),

               NULL, --sub_promotion_id,
               NULL, --sub_promotion_name,
               NULL, --sub_promotion_type,

               rec_rpt_dtl.award_amount, --award_amount,
               rec_claim.proxy_user_id,
               DECODE(rec_claim.proxy_user_id,'','',FNC_GET_USER_NAME(rec_claim.proxy_user_id)),  --Bug # 30618 Fix --v_get_uname,--FNC_GET_USER_NAME(rec_claim.proxy_user_id)

               rec_claim.submitter_id,
               rec_rpt_dtl.submitter_name,
               rec_rpt_dtl.submitter_last_name,
               rec_rpt_dtl.submitter_first_name,
               rec_rpt_dtl.submitter_middle_name,
               NVL(rec_rpt_dtl.submitter_pax_status,' '),
               NVL(rec_rpt_dtl.submitter_job_position,' '),
               NVL(rec_rpt_dtl.submitter_department,' '),
                           rec_claim.node_id,
               rec_rpt_dtl.node_name,

               rec_rpt_dtl.claim_company_name,
               rec_rpt_dtl.claim_first_name,
               rec_rpt_dtl.claim_last_name,
               rec_rpt_dtl.claim_addr1,
               rec_rpt_dtl.claim_addr2,
               rec_rpt_dtl.claim_phone1,
               rec_rpt_dtl.claim_phone2,
               rec_rpt_dtl.claim_email1,
               rec_rpt_dtl.claim_email2,

               SYSDATE,
               p_user_id,
               rec_rpt_dtl.submitter_country_id,
               rec_rpt_dtl.award_amount,               
               rec_rpt_dtl.submitter_sweepstakes_won,
                  rec_claim.badges_earned, --10/25/2012
               1
               );
    
    END IF;
  
  END LOOP;

  FOR rec_sub_info_changed IN cur_sub_info_changed LOOP

    IF rec_sub_info_changed.pax_status_old <> rec_sub_info_changed.pax_status OR
       rec_sub_info_changed.job_position_old <> rec_sub_info_changed.position_type OR
       rec_sub_info_changed.department_old <> rec_sub_info_changed.department_type THEN
      -- rec_sub_info_changed.submitter_badges_earned_new <> rec_sub_info_changed.submitter_badges_earned THEN
       

      v_stage := 'Get hier_level for node_id: '||rec_sub_info_changed.node_id;
      BEGIN
        SELECT parent_node_id header_node_id,
               node_id,
               hier_level,
               is_leaf
          INTO v_header_node_id,
               v_node_id,
               v_hier_level,
               v_is_leaf
          FROM rpt_hierarchy
         WHERE node_id = rec_sub_info_changed.node_id;
      END;

      BEGIN
        v_stage := 'Check Submitter teamsum Old Summary rec for node_id: '||rec_sub_info_changed.node_id;
        SELECT rpt_claim_summary_id,
               claim_count
          INTO v_summ_id,
               v_claim_cnt
          FROM rpt_claim_summary
         WHERE record_type      = v_hier_level||'-teamsum'
           AND NVL(header_node_id, 0) = NVL(v_header_node_id, 0)
           AND detail_node_id   = rec_sub_info_changed.node_id
           AND status           = rec_sub_info_changed.claim_status
           AND date_submitted   = rec_sub_info_changed.date_submitted
           AND promotion_id     = rec_sub_info_changed.promotion_id
           AND award_type       = rec_sub_info_changed.award_type
           AND pax_status       = rec_sub_info_changed.pax_status_old
           AND NVL(job_position,' ')     = rec_sub_info_changed.job_position_old     --Bug 61147
           AND NVL(department,' ')       = rec_sub_info_changed.department_old        --Bug 61147
           AND hier_level       = v_hier_level
           AND is_leaf          = 1;
    
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          v_claim_cnt := 0;
      END;

      IF v_claim_cnt = 1 OR (rec_sub_info_changed.claim_count = v_claim_cnt) THEN

        v_stage := 'Delete Submitter teamsum Old Summary rec for node_id: '||rec_sub_info_changed.node_id;
        DELETE FROM rpt_claim_summary
         WHERE rpt_claim_summary_id = v_summ_id; 

        FOR rec_hier IN cur_hier (rec_sub_info_changed.node_id) LOOP

          BEGIN
 
            v_stage := 'Check Submitter nodesum Old Summary rec for node_id: '||rec_hier.node_id;
            SELECT rpt_claim_summary_id,
                   claim_count
              INTO v_summ_id,
                   v_claim_cnt
              FROM rpt_claim_summary
             WHERE record_type      = rec_hier.hier_level||'-nodesum'
               AND NVL(header_node_id, 0) = NVL(rec_hier.header_node_id, 0)
               AND detail_node_id   = rec_hier.node_id
               AND status           = rec_sub_info_changed.claim_status
               AND date_submitted   = rec_sub_info_changed.date_submitted
               AND promotion_id     = rec_sub_info_changed.promotion_id
               AND award_type       = rec_sub_info_changed.award_type
               AND pax_status       = rec_sub_info_changed.pax_status_old
               AND NVL(job_position,' ')     = rec_sub_info_changed.job_position_old    --Bug 61147
               AND NVL(department,' ')       = rec_sub_info_changed.department_old        --Bug 61147
               AND hier_level       = rec_hier.hier_level
               AND NVL(is_leaf,0)   = NVL(rec_hier.is_leaf,0);
            
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              v_claim_cnt := 0;               
          END;
         
          IF v_claim_cnt = 1 OR (rec_sub_info_changed.claim_count = v_claim_cnt) THEN
          
            v_stage := 'Delete Submitter nodesum Old Summary rec for node_id: '||rec_hier.node_id;
            DELETE FROM rpt_claim_summary
             WHERE rpt_claim_summary_id = v_summ_id; 

          ELSIF v_claim_cnt > rec_sub_info_changed.claim_count THEN
          
            v_stage := 'Update Submitter nodesum Old Summary rec for node_id: '||rec_hier.node_id;
            UPDATE rpt_claim_summary
               SET claim_count = claim_count - rec_sub_info_changed.claim_count,
                   award_amount = award_amount - rec_sub_info_changed.award_amount 
             WHERE rpt_claim_summary_id = v_summ_id;            
          
          END IF;
          
        END LOOP;
      
      ELSIF v_claim_cnt > rec_sub_info_changed.claim_count THEN
      
        v_stage := 'Update Submitter teamsum Old Summary rec for node_id: '||rec_sub_info_changed.node_id;
        UPDATE rpt_claim_summary
           SET claim_count = claim_count - rec_sub_info_changed.claim_count,
               award_amount = award_amount - rec_sub_info_changed.award_amount 
         WHERE rpt_claim_summary_id = v_summ_id;            
      
        FOR rec_hier IN cur_hier (rec_sub_info_changed.node_id) LOOP

          v_stage := 'Update Submitter nodesum Old Summary rec count to - 1 for node_id: '||rec_hier.node_id;
          UPDATE rpt_claim_summary
             SET claim_count = claim_count - rec_sub_info_changed.claim_count,
                 award_amount = award_amount - rec_sub_info_changed.award_amount
             WHERE record_type      = rec_hier.hier_level||'-nodesum'
             AND NVL(header_node_id, 0) = NVL(rec_hier.header_node_id, 0)
             AND detail_node_id   = rec_hier.node_id
             AND status           = rec_sub_info_changed.claim_status
             AND date_submitted   = rec_sub_info_changed.date_submitted
             AND promotion_id     = rec_sub_info_changed.promotion_id
             AND award_type       = rec_sub_info_changed.award_type
             AND pax_status       = rec_sub_info_changed.pax_status_old
             AND NVL(job_position,' ')     = rec_sub_info_changed.job_position_old    --Bug 61147
             AND NVL(department,' ')       = rec_sub_info_changed.department_old    --Bug 61147
             AND hier_level       = rec_hier.hier_level
             AND NVL(is_leaf,0)   = NVL(rec_hier.is_leaf,0);
        
        END LOOP;
      
      END IF;

      BEGIN
        v_stage := 'Check Submitter teamsum New Summary rec for node_id: '||rec_sub_info_changed.node_id;
        SELECT rpt_claim_summary_id,
               claim_count
          INTO v_summ_id,
               v_claim_cnt
          FROM rpt_claim_summary
         WHERE record_type      = v_hier_level||'-teamsum'
           AND NVL(header_node_id, 0) = NVL(v_header_node_id, 0)
           AND detail_node_id   = rec_sub_info_changed.node_id
           AND status           = rec_sub_info_changed.claim_status
           AND date_submitted   = rec_sub_info_changed.date_submitted
           AND promotion_id     = rec_sub_info_changed.promotion_id
           AND award_type       = rec_sub_info_changed.award_type
           AND pax_status       = rec_sub_info_changed.pax_status
           AND NVL(job_position,' ')     = rec_sub_info_changed.position_type    --Bug 61147
           AND NVL(department,' ')       = rec_sub_info_changed.department_type    --Bug 61147
           AND hier_level       = v_hier_level
           AND is_leaf          = 1;

      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          v_claim_cnt := 0;
      END;
      
      IF v_claim_cnt > 0 THEN

        v_stage := 'Update Submitter teamsum New Summary rec for node_id: '||rec_sub_info_changed.node_id;
        UPDATE rpt_claim_summary
           SET claim_count = claim_count + rec_sub_info_changed.claim_count,
               award_amount = award_amount + rec_sub_info_changed.award_amount,
               submitter_sweepstakes_won  = submitter_sweepstakes_won + rec_sub_info_changed.submitter_sweepstakes_won
         WHERE rpt_claim_summary_id = v_summ_id;            

        FOR rec_hier IN cur_hier (rec_sub_info_changed.node_id) LOOP

          v_stage := 'Update Submitter nodesum New Summary rec count to + 1 for node_id: '||rec_hier.node_id;
          UPDATE rpt_claim_summary
             SET claim_count = claim_count + rec_sub_info_changed.claim_count,
                 award_amount = award_amount + rec_sub_info_changed.award_amount
           WHERE record_type      = v_hier_level||'-nodesum'
             AND NVL(header_node_id, 0) = NVL(rec_hier.header_node_id, 0)
             AND detail_node_id   = rec_hier.node_id
             AND status           = rec_sub_info_changed.claim_status
             AND date_submitted   = rec_sub_info_changed.date_submitted
             AND promotion_id     = rec_sub_info_changed.promotion_id
             AND award_type       = rec_sub_info_changed.award_type
             AND pax_status       = rec_sub_info_changed.pax_status_old
             AND NVL(job_position,' ')     = rec_sub_info_changed.job_position_old    --Bug 61147
             AND NVL(department,' ')       = rec_sub_info_changed.department_old    --Bug 61147
             AND hier_level       = rec_hier.hier_level
             AND NVL(is_leaf,0)   = NVL(rec_hier.is_leaf,0);
      
        END LOOP;
      
      ELSE

        v_stage := 'Insert Receiver teamsum New Summary rec for node_id: '||rec_sub_info_changed.node_id;
        INSERT INTO rpt_claim_summary
               (record_type,
                header_node_id,
                detail_node_id,
                status,
                claim_count,
                date_submitted,
                award_amount,
                promotion_id,
                award_type,
                pax_status,
                job_position,
                department,
                hier_level,
                is_leaf,
                rpt_claim_summary_id,
                date_created,
                created_by,                
                submitter_sweepstakes_won,
                submitter_badges_earned)
        VALUES (v_hier_level||'-teamsum',
                v_header_node_id,
                rec_sub_info_changed.node_id,
                rec_sub_info_changed.claim_status,
                rec_sub_info_changed.claim_count,
                rec_sub_info_changed.date_submitted,
                rec_sub_info_changed.award_amount,
                rec_sub_info_changed.promotion_id,
                rec_sub_info_changed.award_type,
                rec_sub_info_changed.pax_status,
                rec_sub_info_changed.position_type,
                rec_sub_info_changed.department_type,
                v_hier_level,
                1,
                rpt_claim_summary_pk_sq.NEXTVAL,
                SYSDATE,
                p_user_id,                
                rec_sub_info_changed.submitter_sweepstakes_won,
                rec_sub_info_changed.submitter_badges_earned
                );

        FOR rec_hier IN cur_hier (rec_sub_info_changed.node_id) LOOP

          BEGIN 
            v_stage := 'Check Submitter nodesum New Summary rec for node_id: '||rec_hier.node_id;
            SELECT rpt_claim_summary_id,
                   claim_count
              INTO v_summ_id,
                   v_claim_cnt
              FROM rpt_claim_summary
             WHERE record_type      = rec_hier.hier_level||'-nodesum'
               AND NVL(header_node_id, 0) = NVL(rec_hier.header_node_id, 0)
               AND detail_node_id   = rec_hier.node_id
               AND status           = rec_sub_info_changed.claim_status
               AND date_submitted   = rec_sub_info_changed.date_submitted
               AND promotion_id     = rec_sub_info_changed.promotion_id
               AND award_type       = rec_sub_info_changed.award_type
               AND pax_status       = rec_sub_info_changed.pax_status
               AND NVL(job_position,' ')     = rec_sub_info_changed.position_type    --Bug 61147
               AND NVL(department,' ')       = rec_sub_info_changed.department_type    --Bug 61147
               AND hier_level       = rec_hier.hier_level
               AND NVL(is_leaf,0)   = NVL(rec_hier.is_leaf,0);
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              v_claim_cnt := 0;
          END;

          IF v_claim_cnt > 0 THEN

            v_stage := 'Update Submitter nodesum New Summary rec count for node_id: '||rec_hier.node_id;
            UPDATE rpt_claim_summary
               SET claim_count = claim_count + rec_sub_info_changed.claim_count,
                   award_amount = award_amount + rec_sub_info_changed.award_amount,
                   submitter_sweepstakes_won  = submitter_sweepstakes_won + rec_sub_info_changed.submitter_sweepstakes_won
             WHERE rpt_claim_summary_id = v_summ_id;
          
          ELSE

            v_stage := 'Insert Receiver nodesum New Summary rec for node_id: '||rec_sub_info_changed.node_id;
            INSERT INTO rpt_claim_summary
                   (record_type,
                    header_node_id,
                    detail_node_id,
                    status,
                    claim_count,
                    date_submitted,
                    award_amount,
                    promotion_id,
                    award_type,
                    pax_status,
                    job_position,
                    department,
                    hier_level,
                    is_leaf,
                    rpt_claim_summary_id,
                    date_created,
                    created_by,                    
                    submitter_sweepstakes_won,
                    submitter_badges_earned
                    )
            VALUES (rec_hier.hier_level||'-nodesum',
                    rec_hier.header_node_id,
                    rec_hier.node_id,
                    rec_sub_info_changed.claim_status,
                    rec_sub_info_changed.claim_count,
                    rec_sub_info_changed.date_submitted,
                    rec_sub_info_changed.award_amount,
                    rec_sub_info_changed.promotion_id,
                    rec_sub_info_changed.award_type,
                    rec_sub_info_changed.pax_status,
                    rec_sub_info_changed.position_type,
                    rec_sub_info_changed.department_type,
                    rec_hier.hier_level,
                    rec_hier.is_leaf,
                    rpt_claim_summary_pk_sq.NEXTVAL,
                    SYSDATE,
                    p_user_id,                    
                    rec_sub_info_changed.submitter_sweepstakes_won,
                    rec_sub_info_changed.submitter_badges_earned
                    );

          END IF;
          
        END LOOP;
      
      END IF;

    END IF;
    
    v_stage := 'Update rpt_claim_detail for pax info changed'; 
    UPDATE rpt_claim_detail
       SET submitter_name         = submitter_name,
           submitter_last_name    = rec_sub_info_changed.last_name,
           submitter_first_name   = rec_sub_info_changed.first_name,
           submitter_middle_name  = rec_sub_info_changed.middle_name,
           submitter_pax_status   = rec_sub_info_changed.pax_status,
           submitter_job_position = rec_sub_info_changed.position_type,
           submitter_department   = rec_sub_info_changed.department_type,
           modified_by            = p_user_id,
           --date_modified          = SYSDATE,
           version                = version + 1
     WHERE submitter_user_id      = rec_sub_info_changed.user_id;           
  END LOOP;

  v_stage := 'Open and Fetch cursor to pick modified node name';
  OPEN cur_node_changed;
  FETCH cur_node_changed BULK COLLECT
   INTO v_tab_node_id,
        v_tab_node_name;
  CLOSE cur_node_changed;   

  v_stage := 'Update modified node name for submitter in rpt table';
  FORALL indx IN v_tab_node_id.FIRST .. v_tab_node_id.LAST
    UPDATE rpt_claim_detail
       SET node_name = DECODE (node_id, v_tab_node_id(indx), v_tab_node_name(indx), node_name),
           date_modified   = SYSDATE,
           modified_by     = p_user_id,
           version         = version + 1
     WHERE (    node_id    = v_tab_node_id(indx)
            AND node_name != v_tab_node_name(indx)
            );
            
    FOR rec_reversed IN cur_reversed(p_start_date,p_end_date)  LOOP --06/02/2014 Bug # 51044

    v_stage := 'Deduct Summary record for reversed journals';

    UPDATE rpt_claim_summary
       SET award_amount = award_amount-rec_reversed.award_amount
     WHERE promotion_id             = rec_reversed.promotion_id
       AND pax_status               = rec_reversed.submitter_pax_status
       AND NVL(job_position,' ')    = rec_reversed.submitter_job_position        --Bug 61147
       AND NVL(department,' ')      = rec_reversed.submitter_department;        --Bug 61147
                
    v_stage := 'Remove award amount from detail record for reversed journals';

    UPDATE rpt_claim_detail
       SET award_amount = award_amount-rec_reversed.award_amount
     WHERE rpt_claim_detail_id = rec_reversed.rpt_claim_detail_id;

    END LOOP;  

  v_stage := 'Procedure Completed';
  prc_execution_log_entry(v_process_name, v_release_level, 'INFO', 
                          v_stage||' for Input '||v_param, NULL);

  p_return_code := 0;
    
EXCEPTION
   WHEN OTHERS THEN
     p_return_code := 99;
     p_error_message := SUBSTR(SQLERRM,1,250);
     prc_execution_log_entry(v_process_name,v_release_level,'ERROR','Error at stage: '||
                             v_stage||'-->'||v_param||' --> '||SQLERRM,null);

END prc_claim_detail;     


/***** Arun S  09/13/2012, Commented out complete delete and insert method
PROCEDURE prc_claim_summary
IS
--
--
-- Purpose: Populate rpt_claim_summary table.
--
-- MODIFICATION HISTORY
-- Person      Date       Comments
-- ---------   ------     ------------------------------------------
-- Bethke      11/11/2005 Initial code.
-- D Murray    12/06/2005 Added nvl to all queries for promotion_id, award_type,
--                        and claim_status - can not have null for
--                        Jasper report parameters.
-- D Murray    12/07/2005 Added pax_status, job_position and department.
-- Rachel R    01/13/2006  Added refresh date logic for summary, pie, bar, and trend.

  c_created_by  CONSTANT RPT_CLAIM_DETAIL.created_by%TYPE:=0;
  v_stage       VARCHAR2(250);
  v_commit_cnt  INTEGER;
  v_rpt_claim_summary_id RPT_CLAIM_SUMMARY.rpt_claim_summary_id%TYPE;

   CURSOR cur_level IS
     SELECT DISTINCT hier_level
       FROM RPT_HIERARCHY
      ORDER BY hier_level DESC;

   CURSOR cur_hier (p_hier_level NUMBER) IS
     SELECT parent_node_id, node_id, is_leaf
       FROM RPT_HIERARCHY
      WHERE hier_level = p_hier_level
      ORDER BY parent_node_id, node_id;

BEGIN

  DELETE  RPT_CLAIM_SUMMARY;


    MERGE INTO RPT_REFRESH_DATE B
    USING (
        SELECT 'claimssubmitted' AS report_name, 'summary' AS report_type, SYSDATE AS refresh_date
        FROM dual
    UNION
         SELECT 'claimssubmitted' AS report_name, 'trend' AS report_type, SYSDATE AS refresh_date
        FROM dual
    UNION
         SELECT 'claimssubmitted' AS report_name, 'piechart' AS report_type, SYSDATE AS refresh_date
        FROM dual
    UNION
         SELECT 'claimssubmitted' AS report_name, 'barchart' AS report_type, SYSDATE AS refresh_date
        FROM dual) E
    ON (B.report_name = e.report_name AND b.report_type = e.report_type)
    WHEN MATCHED THEN
        UPDATE SET B.refresh_date = e.refresh_date
    WHEN NOT MATCHED THEN
        INSERT (B.report_name, b.report_type, b.refresh_date)
        VALUES (E.report_name, E.report_type, e.refresh_date) ;

  FOR rec_level IN cur_level LOOP

    SELECT rpt_claim_summary_pk_sq.NEXTVAL
    INTO v_rpt_claim_summary_id
    FROM dual;

    --insert teamsum records
    INSERT INTO RPT_CLAIM_SUMMARY
           (record_type,
            header_node_id,
            detail_node_id,
            status,
            claim_count,
            date_submitted,
            award_amount,
            promotion_id,
            award_type,
            pax_status,
            job_position,
            department,
            hier_level,
            is_leaf,
            rpt_claim_summary_id,
            date_created,
            created_by)
    (SELECT h.hier_level||'-teamsum'
            ,h.parent_node_id
            ,NVL(d.node_id,h.node_id)
            ,NVL(d.claim_status,' ') claim_status
            ,COUNT(d.claim_id)
            ,TRUNC(d.date_submitted) date_submitted
            ,SUM(d.award_amount)
            ,NVL(d.promotion_id,0) promotion_id
            ,NVL(d.award_type,' ') award_type
            ,NVL(d.submitter_pax_status,' ') pax_status
            ,NVL(d.submitter_job_position,' ') job_position
            ,NVL(d.submitter_department,' ') department
            ,h.hier_level
            ,1  -- always 1 for teamsum
            ,v_rpt_claim_summary_id
            ,SYSDATE
            ,c_created_by
       FROM RPT_CLAIM_DETAIL d
           ,RPT_HIERARCHY h
      WHERE h.node_id = d.node_id (+)
        AND h.hier_level = rec_level.hier_level
      GROUP BY h.parent_node_id ,NVL(d.node_id,h.node_id),d.claim_status,d.promotion_id,
               d.award_type,d.submitter_pax_status,d.submitter_job_position,
               d.submitter_department,date_submitted,h.hier_level);

    v_commit_cnt := SQL%ROWCOUNT + 1;

    FOR rec_hier IN cur_hier(rec_level.hier_level) LOOP

      IF rec_hier.is_leaf = 1 THEN --they have no level below them

        SELECT rpt_claim_summary_pk_sq.NEXTVAL
        INTO v_rpt_claim_summary_id
        FROM dual;

        --insert the same exact records as the teamsum insert
        --except change the record type
        INSERT INTO RPT_CLAIM_SUMMARY
              (record_type,
               header_node_id,
               detail_node_id,
               status,
               claim_count,
               date_submitted,
               award_amount,
               promotion_id,
               award_type,
               pax_status,
               job_position,
               department,
               hier_level,
               is_leaf,
               rpt_claim_summary_id,
               date_created,
               created_by)
        (SELECT d.hier_level||'-nodesum'
                ,d.header_node_id
                ,d.detail_node_id
                ,d.status
                ,d.claim_count
                ,d.date_submitted
                ,d.award_amount
                ,d.promotion_id
                ,d.award_type
                ,d.pax_status
                ,d.job_position
                ,d.department
                ,d.hier_level
                ,d.is_leaf
                ,v_rpt_claim_summary_id
                ,SYSDATE
                ,c_created_by
           FROM RPT_CLAIM_SUMMARY d
          WHERE d.hier_level = rec_level.hier_level
          AND   d.detail_node_id = rec_hier.node_id);

          v_commit_cnt := SQL%ROWCOUNT + 1;

      ELSE --rec_hier.is_leaf != 1

        SELECT rpt_claim_summary_pk_sq.NEXTVAL
        INTO v_rpt_claim_summary_id
        FROM dual;

        --sum the node and the team records
        INSERT INTO RPT_CLAIM_SUMMARY
                       (record_type,
                       header_node_id,
                       detail_node_id,
                       status,
                       claim_count,
                       date_submitted,
                       award_amount,
                       promotion_id,
                       award_type,
                       pax_status,
                       job_position,
                       department,
                       hier_level,
                       is_leaf,
                       rpt_claim_summary_id,
                       date_created,
                       created_by)
        (SELECT a.record_type,
                a.parent_node_id,
                a.node_id,
                NVL(a.status,' ') status,
                SUM(a.claim_count),
                TRUNC( a.date_submitted ) date_submitted,
                SUM(a.award_amount),
                NVL(a.promotion_id,0) promotion_id,
                NVL(a.award_type,' ') award_type,
                NVL(a.pax_status,' ') pax_status,
                NVL(a.job_position,' ') job_position,
                NVL(a.department,' ') department,
                a.hier_level,
                rec_hier.is_leaf,
                v_rpt_claim_summary_id,
                SYSDATE,
                c_created_by
        FROM (
        SELECT h.hier_level||'-nodesum' record_type
                        ,h.parent_node_id
                        ,h.node_id
                        ,nodesum.status
                        ,nodesum.claim_count
                        ,nodesum.date_submitted
                        ,nodesum.award_amount
                        ,nodesum.promotion_id
                        ,nodesum.award_type
                        ,nodesum.pax_status
                        ,nodesum.job_position
                        ,nodesum.department
                        ,h.hier_level
                   FROM RPT_CLAIM_SUMMARY nodesum
                       ,RPT_HIERARCHY h
                  WHERE h.node_id = nodesum.header_node_id
                  AND   nodesum.hier_level = rec_level.hier_level+1
                  AND   nodesum.record_type LIKE '%node%'
                  AND   nodesum.header_node_id = rec_hier.node_id
                  AND   nodesum.claim_count != 0
        UNION ALL
        SELECT nodesum.hier_level||'-nodesum' record_type
                        ,nodesum.header_node_id
                        ,nodesum.detail_node_id
                        ,nodesum.status
                        ,nodesum.claim_count
                        ,nodesum.date_submitted
                        ,nodesum.award_amount
                        ,nodesum.promotion_id
                        ,nodesum.award_type
                        ,nodesum.pax_status
                        ,nodesum.job_position
                        ,nodesum.department
                        ,nodesum.hier_level
                   FROM RPT_CLAIM_SUMMARY nodesum
                  WHERE nodesum.hier_level = rec_level.hier_level
                  AND   nodesum.record_type LIKE '%team%'
                  AND   nodesum.detail_node_id = rec_hier.node_id
                  AND   nodesum.claim_count != 0
        ) a
        GROUP BY a.record_type,a.parent_node_id,a.node_id,a.date_submitted,a.promotion_id,
                 a.award_type,a.pax_status,a.job_position,a.department,a.status,a.hier_level);

        v_commit_cnt := SQL%ROWCOUNT + 1;

        IF SQL%ROWCOUNT = 0 THEN

          SELECT rpt_claim_summary_pk_sq.NEXTVAL
          INTO v_rpt_claim_summary_id
          FROM dual;

          --if the above insert does not insert any records
          --then insert an empty record for the level
          INSERT INTO RPT_CLAIM_SUMMARY
                 (record_type,
                 header_node_id,
                 detail_node_id,
                 status,
                 claim_count,
                 date_submitted,
                 award_amount,
                 promotion_id,
                 award_type,
                 pax_status,
                 job_position,
                 department,
                 hier_level,
                 is_leaf,
                 rpt_claim_summary_id,
                 date_created,
                 created_by)
          (SELECT h.hier_level||'-nodesum'
                  ,h.parent_node_id
                  ,h.node_id
                  ,NVL(s.status,' ') status
                  ,SUM(s.claim_count)
                  ,TRUNC(s.date_submitted) date_submitted
                  ,SUM(s.award_amount)
                  ,NVL(s.promotion_id,0) promotion_id
                  ,NVL(s.award_type,' ') award_type
                  ,NVL(s.pax_status,' ') pax_status
                  ,NVL(s.job_position,' ') job_position
                  ,NVL(s.department,' ') department
                  ,h.hier_level
                  ,rec_hier.is_leaf
                  ,v_rpt_claim_summary_id
                  ,SYSDATE
                  ,c_created_by
             FROM RPT_CLAIM_SUMMARY s
                 ,RPT_HIERARCHY h
            WHERE h.node_id = s.header_node_id (+)
            AND   s.hier_level = rec_level.hier_level+1
            AND   s.record_type LIKE '%node%'
            AND   s.header_node_id = rec_hier.node_id
            GROUP BY h.parent_node_id ,h.node_id,status,date_submitted,s.promotion_id,
                     s.award_type,s.pax_status,s.job_position,s.department,h.hier_level);

          v_commit_cnt := SQL%ROWCOUNT + 1;

        END IF;

      END IF;

    END LOOP;

    IF v_commit_cnt > 500 THEN

      v_commit_cnt := 0;
    END IF;

  END LOOP; --rec_level

EXCEPTION
  WHEN OTHERS THEN
    prc_execution_log_entry('prc_claim_summary',1,'ERROR',v_stage||' '||SQLERRM,null);
END;
**** Arun S  09/13/2012, Commented out complete delete and insert method*/


PROCEDURE prc_claim_summary
  (p_user_id              IN NUMBER,
   p_start_date           IN DATE, 
   p_end_date             IN DATE, 
   p_return_code          OUT NUMBER,
   p_error_message        OUT VARCHAR2
   )
  IS
  
/*-----------------------------------------------------------------------------
-- Purpose: Populate rpt_claim_summary table.
--
-- MODIFICATION HISTORY
-- Person      Date       Comments
-- ---------   ------     ------------------------------------------
-- Bethke      11/11/2005 Initial code.
-- D Murray    12/06/2005 Added nvl to all queries for promotion_id, award_type,
--                        and claim_status - can not have null for
--                        Jasper report parameters.
-- D Murray    12/07/2005 Added pax_status, job_position and department.
-- Rachel R    01/13/2006  Added refresh date logic for summary, pie, bar, and trend.
-- Arun S      09/13/2012  G5 report changes
                           Modified process with incremental approach based on 
                           input p_start_date and p_end_date
--Ravi Dhanekula 01/14/2014 Fixed the bug # 51003
--Kiran kumar     04/08/2015 Bug # 61147 - pkg_report_claim.prc_claim_detail raising exact fetch returns more than requested number of rows
                              While inserting the hierarchy insertions, there is a chance of same node is parent to more than 1 node at the same level.
                              In this case, instead of inserting new record in summary, updating the claim_count, award_amount for existing record.
                 05/20/2015  added nvl check for header_node_id
-----------------------------------------------------------------------------*/  

  --Cursor main
  CURSOR cur_main IS 
    SELECT DISTINCT TRUNC(date_submitted) date_submitted,
           promotion_id,
           award_type,
           submitter_pax_status,
           submitter_job_position,
           submitter_department,
           node_id
      FROM rpt_claim_detail;
--     WHERE (
--            (
--            date_submitted >  p_start_date
--            AND 
--            date_submitted <= SYSDATE
--            )
--            OR
--            (
--            date_modified  >  p_start_date
--            AND 
--            date_modified  <= SYSDATE
--            )
--            );  

  --Cursor detail
  CURSOR cur_dtl (pi_date_submitted         rpt_claim_detail.date_submitted%TYPE,
                  pi_promotion_id           rpt_claim_detail.promotion_id%TYPE,
                  pi_award_type             rpt_claim_detail.award_type%TYPE,
                  pi_submitter_pax_status   rpt_claim_detail.submitter_pax_status%TYPE,
                  pi_submitter_job_position rpt_claim_detail.submitter_job_position%TYPE,
                  pi_submitter_department   rpt_claim_detail.submitter_department%TYPE,
                  pi_node_id                rpt_claim_detail.node_id%TYPE)
      IS
    SELECT h.hier_level||'-teamsum'    record_type,
           h.parent_node_id            header_node_id,
           NVL(d.node_id,h.node_id)    detail_node_id,
           NVL(d.claim_status,' ')     claim_status,
           COUNT(d.claim_id)           claim_count,
           TRUNC(d.date_submitted)     date_submitted,
           SUM(d.award_amount)         award_amount,
           NVL(d.promotion_id,0)       promotion_id,
           NVL(d.award_type,' ')       award_type,
           NVL(d.submitter_pax_status,' ')     pax_status,
           NVL(d.submitter_job_position,' ')   job_position,
           NVL(d.submitter_department,' ')     department,           
           SUM(d.submitter_sweepstakes_won) submitter_sweepstakes_won,
           d.submitter_badges_earned,
           h.hier_level,
           1                                   is_leaf
      FROM rpt_claim_detail d,
           rpt_hierarchy h
     WHERE h.node_id = d.node_id (+)
       AND TRUNC(d.date_submitted)  = pi_date_submitted
       AND d.promotion_id           = pi_promotion_id
       AND d.award_type             = pi_award_type
       AND d.submitter_pax_status   = pi_submitter_pax_status
       AND d.submitter_job_position = pi_submitter_job_position
       AND d.submitter_department   = pi_submitter_department
       AND d.node_id                = pi_node_id    
--       AND (d.date_submitted        <= SYSDATE OR
--            d.date_modified         <= SYSDATE)  
     GROUP BY h.hier_level,h.parent_node_id,NVL(d.node_id,h.node_id),
              d.claim_status,TRUNC(d.date_submitted),d.promotion_id,d.award_type,
              d.submitter_pax_status,d.submitter_job_position,d.submitter_department,
              --d.submitter_sweepstakes_won,
              d.submitter_badges_earned
     ORDER BY date_submitted, promotion_id, claim_status,detail_node_id;   

  --Cursor for hierarchy
  CURSOR cur_hier (p_in_node_id rpt_hierarchy.node_id%TYPE) IS
    SELECT parent_node_id header_node_id,
           node_id,
           hier_level,
           is_leaf
      FROM rpt_hierarchy
     START WITH node_id = p_in_node_id
   CONNECT BY PRIOR parent_node_id = node_id;       
  
  --Execution log variables
  v_process_name            execution_log.process_name%type  := 'prc_claim_summary';
  v_release_level           execution_log.release_level%type := '1';

  --Procedure variables
  v_stage                   VARCHAR2(250);
  v_rpt_claim_summary_id    rpt_claim_summary.rpt_claim_summary_id%TYPE;
  
BEGIN

DELETE FROM rpt_claim_summary; --Fix for bug # 51003    


  FOR rec_main IN cur_main LOOP

    v_stage := 'Delete rpt_claim_summary';
       

    FOR rec_dtl IN cur_dtl (rec_main.date_submitted,
                            rec_main.promotion_id,
                            rec_main.award_type,
                            rec_main.submitter_pax_status,
                            rec_main.submitter_job_position,
                            rec_main.submitter_department,
                            rec_main.node_id) LOOP
 
        v_stage := 'Insert rpt_claim_summary for teamsum record';
        INSERT INTO rpt_claim_summary
               (record_type,
                header_node_id,
                detail_node_id,
                status,
                claim_count,
                date_submitted,
                award_amount,
                promotion_id,
                award_type,
                pax_status,
                job_position,
                department,
                hier_level,
                is_leaf,
                rpt_claim_summary_id,
                date_created,
                created_by,                
                submitter_sweepstakes_won,
                submitter_badges_earned)
        VALUES (rec_dtl.record_type,
                rec_dtl.header_node_id,
                rec_dtl.detail_node_id,
                rec_dtl.claim_status,
                rec_dtl.claim_count,
                rec_dtl.date_submitted,
                rec_dtl.award_amount,
                rec_dtl.promotion_id,
                rec_dtl.award_type,
                rec_dtl.pax_status,
                rec_dtl.job_position,
                rec_dtl.department,
                rec_dtl.hier_level,
                rec_dtl.is_leaf,
                rpt_claim_summary_pk_sq.NEXTVAL,
                SYSDATE,
                p_user_id,                
                rec_dtl.submitter_sweepstakes_won,
                rec_dtl.submitter_badges_earned
                );
                
 --prc_execution_log_entry(v_process_name, v_release_level, 'ERROR',v_stage||' '||SQLERRM||rec_dtl.detail_node_id,NULL);

      FOR rec_hier IN cur_hier (rec_dtl.detail_node_id) LOOP

        --Bug # 61147 Commented -  inserting duplicates entries 
          /*
          v_stage := 'Insert rpt_claim_summary for nodesum record';
          INSERT INTO rpt_claim_summary
                 (record_type,
                  header_node_id,
                  detail_node_id,
                  status,
                  claim_count,
                  date_submitted,
                  award_amount,
                  promotion_id,
                  award_type,
                  pax_status,
                  job_position,
                  department,
                  hier_level,
                  is_leaf,
                  rpt_claim_summary_id,
                  date_created,
                  created_by,                  
                  submitter_sweepstakes_won,
                  submitter_badges_earned)
          VALUES (rec_hier.hier_level||'-nodesum',
                  rec_hier.header_node_id,
                  rec_hier.node_id,
                  rec_dtl.claim_status,
                  rec_dtl.claim_count,
                  rec_dtl.date_submitted,
                  rec_dtl.award_amount,
                  rec_dtl.promotion_id,
                  rec_dtl.award_type,
                  rec_dtl.pax_status,
                  rec_dtl.job_position,
                  rec_dtl.department,
                  rec_hier.hier_level,
                  rec_hier.is_leaf,
                  rpt_claim_summary_pk_sq.NEXTVAL,
                  SYSDATE,
                  p_user_id,                  
                  rec_dtl.submitter_sweepstakes_won,
                  rec_dtl.submitter_badges_earned
                  );
            */
            --Bug # 61147 Starts
            MERGE INTO rpt_claim_summary T
            USING (
               SELECT rec_hier.hier_level||'-nodesum' record_type,
                      rec_hier.header_node_id header_node_id,
                      rec_hier.node_id detail_node_id,
                      rec_dtl.claim_status status,
                      rec_dtl.claim_count claim_count,
                      rec_dtl.date_submitted date_submitted,
                      rec_dtl.award_amount award_amount,
                      rec_dtl.promotion_id promotion_id,
                      rec_dtl.award_type award_type,
                      rec_dtl.pax_status pax_status,
                      rec_dtl.job_position job_position,
                      rec_dtl.department department,
                      rec_hier.hier_level hier_level,
                      rec_hier.is_leaf is_leaf,
                      rec_dtl.submitter_sweepstakes_won submitter_sweepstakes_won,
                      rec_dtl.submitter_badges_earned submitter_badges_earned
                 FROM DUAL
            ) S
            ON ( T.record_type = S. record_type
               AND NVL(T.header_node_id,0) = NVL(S.header_node_id,0)  --05/20/2015
               AND T.detail_node_id = S.detail_node_id
               AND T.status = S.status
               AND T.date_submitted = S.date_submitted
               AND T.promotion_id = S.promotion_id
               AND T.award_type = S.award_type
               AND T.pax_status = S.pax_status
               AND nvl(T.job_position,' ') = nvl(S.job_position,' ')
               AND nvl(T.department,' ') = nvl(S.department,' ')
               AND T.hier_level = S.hier_level
               AND NVL(T.is_leaf,0) = NVL(S.is_leaf,0)                 --05/20/2015
               )
            WHEN MATCHED THEN
              UPDATE 
                 SET T.claim_count  = NVL(T.claim_count,0)  + NVL(S.claim_count,0),             --05/20/2015
                     T.award_amount = NVL(T.award_amount,0) + NVL(S.award_amount,0),            --05/20/2015
                     T.submitter_sweepstakes_won = NVL(T.submitter_sweepstakes_won,0) + NVL(S.submitter_sweepstakes_won,0),     --05/20/2015
                     T.submitter_badges_earned   = NVL(T.submitter_badges_earned,0) + NVL(S.submitter_badges_earned,0)          --05/20/2015
            WHEN NOT MATCHED THEN
              INSERT (
                       T.record_type,
                       T.header_node_id,
                       T.detail_node_id,
                       T.status,
                       T.claim_count,
                       T.date_submitted,
                       T.award_amount,
                       T.promotion_id,
                       T.award_type,
                       T.pax_status,
                       T.job_position,
                       T.department,
                       T.hier_level,
                       T.is_leaf,
                       T.rpt_claim_summary_id,
                       T.date_created,
                       T.created_by,                  
                       T.submitter_sweepstakes_won,
                       T.submitter_badges_earned
              ) VALUES(
                       S.record_type,
                       S.header_node_id,
                       S.detail_node_id,
                       S.status,
                       S.claim_count,
                       S.date_submitted,
                       S.award_amount,
                       S.promotion_id,
                       S.award_type,
                       S.pax_status,
                       S.job_position,
                       S.department,
                       S.hier_level,
                       S.is_leaf,
                       rpt_claim_summary_pk_sq.NEXTVAL,
                       SYSDATE,
                       p_user_id,                  
                       S.submitter_sweepstakes_won,
                       S.submitter_badges_earned
              );
              --Bug # 61147 Ends
      END LOOP;    
  
    END LOOP;
    
  END LOOP;  

   v_stage := 'INSERT missing default team summary permutations';
   INSERT INTO rpt_claim_summary
               (record_type,
                header_node_id,
                detail_node_id,
                status,
                claim_count,
                date_submitted,
                award_amount,
                promotion_id,
                award_type,
                pax_status,
                job_position,
                department,
                hier_level,
                is_leaf,
                rpt_claim_summary_id,
                date_created,
                created_by,                
                submitter_sweepstakes_won,
                submitter_badges_earned)
        (SELECT nsp.record_type, 
                nsp.header_node_id, 
                nsp.detail_node_id, 
                NULL status,
                0 claim_count,
                NULL date_submitted,
                NULL award_amount,
                0 promotion_id,
                NULL award_type,
                NULL pax_status,
                NULL job_position,
                NULL department,
                nsp.hier_level,
                nsp.is_leaf,
                rpt_claim_summary_pk_sq.NEXTVAL,
                SYSDATE, 
                p_user_id,               
                0,
                0
           FROM ( -- get all possible node summary type permutations
                 SELECT h.node_id AS detail_node_id,
                        h.hier_level || '-' || 'teamsum' AS record_type,
                        h.parent_node_id AS header_node_id,
                        h.hier_level,
                        1 AS is_leaf   -- team summary always a leaf node
                   FROM rpt_hierarchy h
                   ) nsp                          
                 -- exclude default permutation when a matching summary record exists
          WHERE NOT EXISTS
                     ( SELECT 1
                         FROM rpt_claim_summary s
                        WHERE s.detail_node_id = nsp.detail_node_id
                          AND s.record_type    = nsp.record_type
                      )
                      );

  p_return_code := 0;

EXCEPTION
  WHEN OTHERS THEN
    p_return_code := 99;
    p_error_message := SUBSTR(SQLERRM,1,250);
    prc_execution_log_entry(v_process_name, v_release_level, 'ERROR',v_stage||' '||SQLERRM,NULL);

END;     


PROCEDURE p_claim_product_summary  IS
/*******************************************************************************
--
-- Purpose: Populate rpt_claim_summary table.
--
-- MODIFICATION HISTORY
-- Person      Date       Comments
-- ---------   ------     ------------------------------------------
-- Raju N      11/28/2005 Initial code.
-- Raju N      12/07/2005 Changed for the new pax columns on the summary and
                          details.
-- D Murray    12/15/2005 Added nvl to all queries for promotion_id, award_type,
--                        pax_status, job_position, department,
--                        product_category_id, and product_subcategory_id
--                        - can not have null for Jasper report parameters.
--                        Added Commit.
TBD -- create records for nodes that do not have activity.
*******************************************************************************/
  c_created_by  CONSTANT RPT_CLAIM_DETAIL.created_by%TYPE:=0;
  v_stage       VARCHAR2(250);
  v_commit_cnt  INTEGER;
  v_rpt_claim_summary_id RPT_CLAIM_SUMMARY.rpt_claim_summary_id%TYPE;

   CURSOR cur_level IS
     SELECT DISTINCT hier_level
       FROM RPT_HIERARCHY
      ORDER BY hier_level DESC;

   CURSOR cur_hier (p_hier_level NUMBER) IS
     SELECT parent_node_id, node_id, is_leaf
       FROM RPT_HIERARCHY
      WHERE hier_level = p_hier_level
      ORDER BY parent_node_id, node_id;

   CURSOR cur_prod_detail (p_node_id IN NUMBER) IS
       SELECT d.claim_id,d.node_id,p.product_id,p.product_name,
              p.product_category_id,p.product_category_name,
              p.product_subcategory_id,p.product_subcategory_name,
              d.promotion_id,d.award_type,d.date_submitted,
              d.submitter_job_position,d.submitter_department,
              d.submitter_pax_status,
              SUM(p.product_qty) total_quantity
         FROM RPT_CLAIM_DETAIL d
              ,RPT_CLAIM_PRODUCT p
        WHERE d.claim_id = p.claim_id
          AND d.node_id = p_node_id
        GROUP BY d.claim_id,d.node_id,p.product_id,p.product_category_id,
                 p.product_subcategory_id,p.product_name,p.product_category_name,
                 p.product_subcategory_name,d.promotion_id,d.award_type,
                 d.date_submitted,d.submitter_job_position,d.submitter_department,
                 d.submitter_pax_status   ;

   CURSOR cur_prod_summary ( p_parent_node_id IN NUMBER)  IS
     SELECT date_submitted,award_type,promotion_id,
            product_category_id, product_sub_category_id,product_id,
            product_name,product_category_name,product_subcategory_name,
            submitter_job_position,submitter_department,submitter_pax_status,
            SUM(total_quantity) total_quantity
       FROM RPT_CLAIM_PRODUCT_SUMMARY
      WHERE header_node_id = p_parent_node_id
      GROUP BY date_submitted,award_type,promotion_id,
               product_name,product_category_name,product_subcategory_name,
               product_category_id, product_sub_category_id,product_id,
               submitter_job_position,submitter_department,submitter_pax_status ;

v_flag BOOLEAN;

BEGIN
   
   DELETE   RPT_CLAIM_PRODUCT_SUMMARY;

       /*MERGE INTO RPT_REFRESH_DATE B
       USING(SELECT 'claimproduct' AS report_name, 'summary' AS report_type, SYSDATE AS refresh_date
             FROM dual) E
       ON (B.report_name = E.report_name AND B.report_type = E.report_type)
       WHEN MATCHED THEN
           UPDATE SET B.refresh_date = E.refresh_date
       WHEN NOT MATCHED THEN
           INSERT (B.report_name, B.report_type, B.refresh_date)
        VALUES (E.report_name, E.report_type, E.refresh_date);
*/
   FOR rec_level IN  cur_level LOOP -- hier level
     FOR rec_hier IN cur_hier(rec_level.hier_level)  LOOP -- hierarchy
        -- create all team summary records
        IF rec_hier.is_leaf = 1 THEN -- there are no child nodes under this
          v_flag := TRUE;
          FOR i IN cur_prod_detail (rec_hier.node_id) LOOP
             v_flag := FALSE;
             INSERT INTO RPT_CLAIM_PRODUCT_SUMMARY
                (rpt_claim_product_summary_id,
                 record_type, header_node_id,
                 detail_node_id, date_submitted,award_type,promotion_id,
                 product_category_id, product_sub_category_id,product_id,
                 product_name,product_category_name,product_subcategory_name,
                 submitter_job_position,submitter_department,submitter_pax_status,
                 total_quantity, hier_level, is_leaf, date_created, created_by
                 )
             VALUES(rpt_claim_product_summary_sq.NEXTVAL,
                 rec_level.hier_level||'-teamsum',rec_hier.parent_node_id,
                 rec_hier.node_id, i.date_submitted, NVL(i.award_type,' '),
                 NVL(i.promotion_id,0),NVL(i.product_category_id,0),
                 NVL(i.product_subcategory_id,0),i.product_id,
                 i.product_name,i.product_category_name,i.product_subcategory_name,
                 NVL(i.submitter_job_position,' '),NVL(i.submitter_department,' '),
                 NVL(i.submitter_pax_status,' '),
                 i.total_quantity, rec_level.hier_level, rec_hier.is_leaf,
                 SYSDATE, c_created_by) ;

             v_commit_cnt := SQL%ROWCOUNT + 1;
           END LOOP ;
           IF v_flag THEN
             INSERT INTO RPT_CLAIM_PRODUCT_SUMMARY
                (rpt_claim_product_summary_id,
                 record_type, header_node_id,
                 detail_node_id, date_submitted,award_type,promotion_id,
                 product_category_id, product_sub_category_id,product_id,
                 product_name,product_category_name,product_subcategory_name,
                 submitter_job_position,submitter_department,submitter_pax_status,
                 total_quantity, hier_level, is_leaf, date_created, created_by
                 )
               VALUES
               (rpt_claim_product_summary_sq.NEXTVAL,
                 rec_level.hier_level||'-teamsum',rec_hier.parent_node_id,
                 rec_hier.node_id, NULL, ' ',
                 0,0,
                 0,0,
                 NULL,NULL,NULL,
                 ' ',' ',
                 ' ',
                 0, rec_level.hier_level, rec_hier.is_leaf,
                 SYSDATE, c_created_by
               );
           END IF;
         ELSIF rec_hier.is_leaf = 0 THEN
         v_flag := TRUE;
          FOR i IN cur_prod_detail (rec_hier.node_id) LOOP --detail loop
            v_flag := FALSE;
             INSERT INTO RPT_CLAIM_PRODUCT_SUMMARY
                (rpt_claim_product_summary_id,
                 record_type, header_node_id,
                 detail_node_id, date_submitted,award_type,promotion_id,
                 product_category_id, product_sub_category_id,product_id,
                 product_name,product_category_name,product_subcategory_name,
                 submitter_job_position,submitter_department,submitter_pax_status,
                 total_quantity, hier_level, is_leaf, date_created, created_by
                 )
             VALUES (RPT_CLAIM_PRODUCT_SUMMARY_SQ.NEXTVAL,
                 rec_level.hier_level||'-teamsum',rec_hier.parent_node_id,
                 rec_hier.node_id, i.date_submitted, NVL(i.award_type,' '),
                 NVL(i.promotion_id,0), NVL(i.product_category_id,0),
                 NVL(i.product_subcategory_id,0),i.product_id,
                 i.product_name,i.product_category_name,i.product_subcategory_name,
                 NVL(i.submitter_job_position,' '),NVL(i.submitter_department,' '),
                 NVL(i.submitter_pax_status,' '),
                 i.total_quantity, rec_level.hier_level, 1,-- leaf for team summary
                 SYSDATE, c_created_by) ;

            v_commit_cnt := SQL%ROWCOUNT + 1;
          END LOOP ; --detail loop
           IF v_flag THEN
             INSERT INTO RPT_CLAIM_PRODUCT_SUMMARY
                (rpt_claim_product_summary_id,
                 record_type, header_node_id,
                 detail_node_id, date_submitted,award_type,promotion_id,
                 product_category_id, product_sub_category_id,product_id,
                 product_name,product_category_name,product_subcategory_name,
                 submitter_job_position,submitter_department,submitter_pax_status,
                 total_quantity, hier_level, is_leaf, date_created, created_by
                 )
               VALUES
               (rpt_claim_product_summary_sq.NEXTVAL,
                 rec_level.hier_level||'-teamsum',rec_hier.parent_node_id,
                 rec_hier.node_id, NULL, ' ',
                 0,0,
                 0,0,
                 NULL,NULL,NULL,
                 ' ',' ',
                 ' ',
                 0, rec_level.hier_level, 1,
                 SYSDATE, c_created_by
               );
           END IF;
          FOR j IN cur_prod_summary (rec_hier.node_id) LOOP -- summary loop
             INSERT INTO RPT_CLAIM_PRODUCT_SUMMARY
                (rpt_claim_product_summary_id,
                 record_type, header_node_id,
                 detail_node_id, date_submitted,award_type,promotion_id,
                 product_category_id, product_sub_category_id,product_id,
                 product_name,product_category_name,product_subcategory_name,
                 submitter_job_position,submitter_department,submitter_pax_status,
                 total_quantity, hier_level, is_leaf, date_created, created_by
                 )
             VALUES (RPT_CLAIM_PRODUCT_SUMMARY_SQ.NEXTVAL,
                 rec_level.hier_level||'-nodesum',rec_hier.parent_node_id,
                 rec_hier.node_id, j.date_submitted, NVL(j.award_type,' '),
                 NVL(j.promotion_id,0), NVL(j.product_category_id,0),
                 NVL(j.product_sub_category_id,0),j.product_id,
                 j.product_name,j.product_category_name,j.product_subcategory_name,
                 NVL(j.submitter_job_position,' '),NVL(j.submitter_department,' '),
                 NVL(j.submitter_pax_status,' '),
                 j.total_quantity, rec_level.hier_level, rec_hier.is_leaf,
                 SYSDATE, c_created_by) ;

            v_commit_cnt := SQL%ROWCOUNT + 1;
          END LOOP ; -- summary loop
        END  IF ;

        IF v_commit_cnt > 500 THEN

          v_commit_cnt := 0;
        END IF;

     END  LOOP ; -- hierarchy
   END  LOOP ;  -- hier level

EXCEPTION
  WHEN OTHERS THEN
    prc_execution_log_entry('p_claim_product_summary',1,'ERROR',v_stage||' '||SQLERRM,null);
END  ;

END;
/
