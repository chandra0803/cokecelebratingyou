CREATE OR REPLACE PACKAGE pkg_report_product_claim
  IS

/*-----------------------------------------------------------------------------
--
-- Purpose: Populate rpt_claim_product table.
--
-- MODIFICATION HISTORY
-- Person      Date     Comments
-- ---------   ------   ------------------------------------------
-- Arun      12/22/2005 Initial code.
-- Arun S    09/18/2012 G5 report changes
                        Added in parameters p_user_id, p_start_date, p_end_date, 
                        p_return_code and p_error_message 

-----------------------------------------------------------------------------*/

   PROCEDURE p_claim_product_summary;

   PROCEDURE prc_claim_detail_for_prd_claim
    (p_user_id              IN NUMBER,
     p_start_date           IN DATE, 
     p_end_date             IN DATE, 
     p_return_code          OUT NUMBER,
     p_error_message        OUT VARCHAR2);

END; -- Package spec
/
CREATE OR REPLACE PACKAGE BODY pkg_report_product_claim
IS

PROCEDURE prc_claim_detail_for_prd_claim
  (p_user_id              IN NUMBER,
   p_start_date           IN DATE, 
   p_end_date             IN DATE, 
   p_return_code          OUT NUMBER,
   p_error_message        OUT VARCHAR2)
IS
/*-----------------------------------------------------------------------------
--
--
-- Purpose: Populate rpt_claim_product table.
--
-- MODIFICATION HISTORY
-- Person      Date        Comments
-- ---------   ------      ------------------------------------------
--                         Initial creation
-- Arun S      09/18/2012  G5 report changes - Modified process in incremental
--                         approach 
-- Swati        06/03/2014  Bug 51044 - Reverse transactions are not accounted for in reporting 
--nagarajs      01/04/2016  Bug 64949 - Reporting showing multiple points awarded for a a tiered product claim promotion
-----------------------------------------------------------------------------*/

  --Cusrsor claim detail
  CURSOR cur_claim_detail IS
    SELECT rpt.claim_id, 
           rpt.claim_status,
           rpt.rpt_claim_detail_id
      FROM rpt_claim_detail rpt,
           claim c,
           promotion p
     WHERE rpt.claim_id       = c.claim_id
       AND c.promotion_id     = p.promotion_id
       AND p.promotion_status IN ('live','expired')
       AND p.is_deleted       = 0
       AND p.promotion_type   = 'product_claim'
       AND (p_start_date      < c.date_created  OR p_start_date    < c.date_modified)
       AND (c.date_created    <= p_end_date     OR c.date_modified <= p_end_date);

  --Cusrsor product detail
  CURSOR cur_prod (p_in_claim_id claim_item.claim_id%TYPE) IS
    SELECT cp.claim_item_id,
           cp.product_id, 
           cp.product_qty,
           ci.serial_id, 
           ca.approver_user_id
      FROM claim_product cp, 
           claim_item ci,
           (SELECT di.claim_item_id, 
                   DECODE(di.approval_status_type,'approv',dia.approver_user_id,NULL) AS approver_user_id
              FROM claim_item_approver dia, 
                   claim_item di
             WHERE di.claim_item_id = dia.claim_item_id
               AND di.claim_id      = p_in_claim_id
            ) ca
     WHERE cp.claim_item_id = ci.claim_item_id
       AND ci.claim_item_id = ca.claim_item_id (+)
       AND ci.claim_id      = p_in_claim_id;

    CURSOR cur_reversed (v_start_date IN DATE, v_end_date IN DATE)  --06/03/2014 Bug # 51044
      IS
      SELECT rpt.*
      FROM rpt_claim_product rpt, activity_journal aj, activity a,journal j
     WHERE     rpt.claim_id = a.claim_id
           AND a.activity_id = aj.activity_id
           AND j.is_reverse = 1 
           AND J.journal_id = aJ.journal_id
           AND (v_start_date   <  J.date_created   AND J.date_created <= v_end_date);
           
  --Execution log variables
  v_process_name            execution_log.process_name%TYPE  := 'prc_claim_detail_for_prd_claim';
  v_release_level           execution_log.release_level%TYPE := '1';
  v_severity_i              execution_log.severity%TYPE := 'INFO';
  v_severity_e              execution_log.severity%TYPE := 'ERROR';

  --Procedure variables
  v_param                   VARCHAR2(250);
  v_stage                   VARCHAR2(250);
  rec_rpt_prod              rpt_claim_product%ROWTYPE;
  v_parent_category_id      product_category.parent_category_id%TYPE;
  v_parent_category_name    product_category.product_category_name%TYPE;
  v_ins_claim_pdt           VARCHAR2(1) := 'N';

BEGIN

  v_stage := 'Procedure Started';
  
  v_param := 'Start Date:'||TO_CHAR(p_start_date,'MM/DD/YYYY HH:MI:SS AM')||
             ' End Date:'||TO_CHAR(p_end_date,'MM/DD/YYYY HH:MI:SS AM');

  prc_execution_log_entry(v_process_name, v_release_level, v_severity_i, 
                          v_stage||' for Input '||v_param, NULL);

  FOR rec_claim_detail IN cur_claim_detail LOOP

    FOR rec_prod IN cur_prod (rec_claim_detail.claim_id) LOOP
    
      rec_rpt_prod := NULL;
      
      BEGIN
        v_stage := 'Find product award amoount';
        SELECT SUM(j.transaction_amt)
          INTO rec_rpt_prod.product_award_amount
          FROM activity a, 
               activity_journal aj, 
               journal j
         WHERE a.activity_id = aj.activity_id
           AND aj.journal_id = j.journal_id
           AND a.claim_id    = rec_claim_detail.claim_id
           AND a.product_id  = rec_prod.product_id
           AND NVL(a.is_carryover,0) = 0            --01/04/2016
           AND j.status_type != 'pend_min_qual';    --01/04/2016
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          rec_rpt_prod.product_award_amount := NULL;
      END;

      IF rec_claim_detail.claim_status = 'closed' THEN
        
        IF rec_prod.approver_user_id IS NOT NULL THEN
          rec_rpt_prod.approver_user_id := rec_prod.approver_user_id;
          rec_rpt_prod.approver_name    := fnc_get_final_appr_name(rec_prod.approver_user_id);                   
        ELSE
          rec_prod.approver_user_id  := NULL;
          rec_rpt_prod.approver_name := NULL;
        END IF;
        
      END IF;
      
      BEGIN
        v_stage := 'Check claim in claim_product';
        SELECT rpt_claim_product_id
          INTO rec_rpt_prod.rpt_claim_product_id
          FROM rpt_claim_product
         WHERE claim_id         = rec_claim_detail.claim_id
           AND claim_product_id = rec_prod.claim_item_id
           AND product_id       = rec_prod.product_id;

        v_stage := 'Update rpt_claim_product';        
        UPDATE rpt_claim_product
           SET product_award_amount = rec_rpt_prod.product_award_amount,
               approver_user_id     = rec_prod.approver_user_id,
               approver_name        = rec_rpt_prod.approver_name,
               date_modified        = SYSDATE,
               modified_by          = p_user_id,
               version              = version + 1
         WHERE rpt_claim_product_id = rec_rpt_prod.rpt_claim_product_id;                

        v_ins_claim_pdt := 'N';

      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          v_ins_claim_pdt := 'Y';         
      END;

      IF v_ins_claim_pdt = 'Y' THEN

        v_stage := 'find product';
        BEGIN
          SELECT product_name,
                 category_id
            INTO rec_rpt_prod.product_name,
                 rec_rpt_prod.product_subcategory_id
            FROM product
           WHERE product_id = rec_prod.product_id;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            rec_rpt_prod.product_name := NULL;
            rec_rpt_prod.product_subcategory_id := NULL;
        END;

        v_stage := 'Find product category';        
        BEGIN
          SELECT parent_category_id, 
                 product_category_name
            INTO v_parent_category_id, 
                 v_parent_category_name
            FROM product_category
           WHERE product_category_id = rec_rpt_prod.product_subcategory_id;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            v_parent_category_id := NULL;
            v_parent_category_name := NULL;
        END;

        IF v_parent_category_id IS NULL THEN
          rec_rpt_prod.product_category_id      := rec_rpt_prod.product_subcategory_id;
          rec_rpt_prod.product_category_name    := v_parent_category_name;
          rec_rpt_prod.product_subcategory_name := NULL;
          rec_rpt_prod.product_subcategory_id   := NULL;
        ELSE

          v_stage := 'find product_sub_category';
          BEGIN
            SELECT product_category_name,
                   parent_category_id
              INTO rec_rpt_prod.product_subcategory_name,
                   rec_rpt_prod.product_category_id
              FROM product_category
             WHERE product_category_id = rec_rpt_prod.product_subcategory_id;
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              rec_rpt_prod.product_subcategory_name := NULL;
              rec_rpt_prod.product_category_id      := NULL;
          END;

          v_stage := 'find product_category';
          BEGIN
            SELECT product_category_name
              INTO rec_rpt_prod.product_category_name
              FROM product_category
             WHERE product_category_id = rec_rpt_prod.product_category_id;
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              rec_rpt_prod.product_category_name := NULL;
          END;
          
        END IF;

        v_stage := 'insert rpt_claim_product';
        INSERT INTO rpt_claim_product
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
                 product_award_amount,
                 version
                 )
          VALUES(rpt_claim_product_pk_sq.NEXTVAL,
                 rec_claim_detail.rpt_claim_detail_id,             --rpt_claim_detail_pk_sq.NEXTVAL,
                 rec_prod.claim_item_id,
                 rec_claim_detail.claim_id,
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
                 p_user_id,
                 rec_rpt_prod.product_award_amount,
                 1
                 );
        
      END IF;    
    
    END LOOP;  
  
  END LOOP;
  
    FOR rec_reversed IN cur_reversed(p_start_date,p_end_date)  LOOP --06/03/2014 Bug # 51044

        v_stage := 'Deduct Summary record for reversed journals';

        UPDATE rpt_claim_product_summary
        SET award_amount = award_amount-rec_reversed.PRODUCT_AWARD_AMOUNT
        WHERE   product_id                 = rec_reversed.product_id
            AND product_category_id     = rec_reversed.product_category_id
            AND product_sub_category_id    = rec_reversed.product_subcategory_id;    
                 
        v_stage := 'Remove award amount from detail record for reversed journals';

        UPDATE rpt_claim_product
        SET PRODUCT_AWARD_AMOUNT = PRODUCT_AWARD_AMOUNT-rec_reversed.PRODUCT_AWARD_AMOUNT
        WHERE
           rpt_claim_detail_id = rec_reversed.rpt_claim_detail_id;                    

    END LOOP;  

  v_stage := 'Procedure Completed';
  prc_execution_log_entry(v_process_name, v_release_level, 'INFO', 
                          v_stage||' for Input '||v_param, NULL);

  p_return_code := 0;
    
EXCEPTION
   WHEN OTHERS THEN
     p_return_code := 99;
     prc_execution_log_entry(v_process_name,v_release_level,v_severity_e,
                             'Error at stage: '||v_stage||' --> '||SQLERRM, NULL);

END;    --prc_claim_detail_for_prd_claim


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
-- Rachel R    01/13/2006  Added refresh date logic for summary, pie, bar, and trend.
-- Raju N      06/21/2006  Code to handle the report drill down to the lowest level.
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

    DELETE  RPT_CLAIM_PRODUCT_SUMMARY;
    
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
                 SYSDATE,c_created_by) ;

            v_commit_cnt := SQL%ROWCOUNT + 1;
          END LOOP ; -- summary loop
        END  IF ;

        IF v_commit_cnt > 500 THEN

          v_commit_cnt := 0;
        END IF;

     END  LOOP ; -- hierarchy
dbms_output.put_line('Level: '||rec_level.hier_level||' Rec ins: '||v_commit_cnt);
   END  LOOP ;  -- hier level

  -- 06/21/2006
  -- Team summary at the lowest level is same as the node summary
  -- Change the team summary record to be the node summary
    UPDATE rpt_claim_product_summary
       SET record_type = replace(record_type,'team','node'),
           created_by = created_by
     WHERE hier_level = (SELECT max( hier_level)  FROM RPT_HIERARCHY) ;
  -- Create team summary records with null header id to allow the user attached to
  -- the lowest level view the report
  INSERT INTO rpt_claim_product_summary--05/26/2017 Added column names in insert Bug # 72822
    (RPT_CLAIM_PRODUCT_SUMMARY_ID, RECORD_TYPE, HEADER_NODE_ID, 
         DETAIL_NODE_ID, DATE_SUBMITTED, AWARD_AMOUNT, PROMOTION_ID,
         AWARD_TYPE, PRODUCT_CATEGORY_ID, PRODUCT_SUB_CATEGORY_ID,
         TOTAL_QUANTITY, HIER_LEVEL, IS_LEAF, PRODUCT_ID,
         SUBMITTER_PAX_STATUS, SUBMITTER_JOB_POSITION,
         SUBMITTER_DEPARTMENT, PRODUCT_NAME, PRODUCT_CATEGORY_NAME,
         PRODUCT_SUBCATEGORY_NAME, DATE_CREATED, CREATED_BY)
  (SELECT  rpt_claim_product_summary_sq.nextval, replace(record_type,'node','team'), null,
         detail_node_id, date_submitted, award_amount, promotion_id,
         award_type, product_category_id, product_sub_category_id,
         total_quantity, hier_level, is_leaf, product_id,
         submitter_pax_status, submitter_job_position,
         submitter_department, product_name, product_category_name,
         product_subcategory_name, date_created, created_by--||'R'
    FROM rpt_claim_product_summary
   WHERE hier_level = (SELECT max( hier_level)  FROM RPT_HIERARCHY)
     AND record_type LIKE '%node%') ;

EXCEPTION
  WHEN OTHERS THEN
    prc_execution_log_entry('rpt_claim_product_summary',1,'ERROR',SQLERRM,null);
END  ;

END;
/
