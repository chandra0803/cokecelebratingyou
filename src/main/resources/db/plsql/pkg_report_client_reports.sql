CREATE OR REPLACE PACKAGE PKG_CLIENT_REPORTS IS
/******************************************************************************
   NAME:       PKG_CLIENT_REPORTS
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        11/28/2007                   1. Created this package.
   1.1        03/07/2008  S. Majumder      Added new procedures P_AWARD_ITEM_SELECTION,
                                           P_AWARD_ORDER_DETAIL_REPORT
   1.2        07/25/2008  M. Lindvig       Added new procedures P_AWARD_EARNINGS_REPORT
              04/01/2019  Suresh J         SA Integeration with DayMaker changes for reports   
******************************************************************************/

   PROCEDURE P_AWARD_ITEM_ACTIVITY(p_in_user_id           IN  NUMBER,  
                                   p_in_start_date        IN  DATE,
                                   p_in_end_date          IN  DATE,
                                   p_out_return_code      OUT NUMBER,
                                   p_out_error_message    OUT VARCHAR2);
   PROCEDURE P_AWARD_ITEM_SELECTION(p_in_user_id           IN  NUMBER,  
                                    p_in_start_date        IN  DATE,
                                    p_in_end_date          IN  DATE,
                                    p_out_return_code      OUT NUMBER,
                                    p_out_error_message    OUT VARCHAR2);
   PROCEDURE P_AWARD_ORDER_DETAIL_REPORT;
   PROCEDURE P_AWARD_EARNINGS_REPORT;
   PROCEDURE P_AWARD_ITEM_ACTIVITY_SA( p_in_user_id           IN  NUMBER,  
                                       p_in_start_date        IN  DATE,
                                       p_in_end_date          IN  DATE,
                                       p_out_return_code      OUT NUMBER,
                                       p_out_error_message    OUT VARCHAR2);
   PROCEDURE P_AWARD_ITEM_SELECTION_SA( p_in_user_id           IN  NUMBER,  
                                        p_in_start_date        IN  DATE,
                                        p_in_end_date          IN  DATE,
                                        p_out_return_code      OUT NUMBER,
                                        p_out_error_message    OUT VARCHAR2);


END PKG_CLIENT_REPORTS;


/
CREATE OR REPLACE PACKAGE BODY PKG_CLIENT_REPORTS
IS
/***********************************************************************************
   Purpose:  To verify or load stage_hierarchy_import_record into original hierarchy tables.

   Person             Date           Comments
   -----------        ----------     --------------------------------------------
   Lindvig, Matthew   12/03/2007     Initial Version
   S. Majumder        03/07/2008     Added new procedures P_AWARD_ITEM_SELECTION
                                     P_AWARD_ORDER_DETAIL_REPORT
   S. Majumder        04/25/2008     Modified P_AWARD_ORDER_DETAIL_REPORT to
                                     populate promotion_name and country_code into 
                                     RPT_AWARD_ORDER_DETAIL table
   M. Lindvig         07/25/2008     Added new procedure P_AWARD_EARNINGS_REPORT and removed P_PROGRAM_REFERENCE_NBR                                 
   S. Majumder        05/12/2009     Bug # 25780  
   Arun S             07/30/2009     When expiration_date is null then treated the gift code expiration as future dated 
   Arun S             04/20/2010     Bug # 31921 fix, When Kit product is Ordered then there may be chance to have multiple 
                                     records in ps_bpom_ms_ship@msmart for a Order, To limit multiple row fetching added 
                                     rownum = 1 in the process p_award_order_detail_report
   Arun S             07/22/2011     Bug # 37598 fix, In P_AWARD_ORDER_DETAIL_REPORT made changes to
                                     use column PS_BPOM_MS_REDMPTN.BPOM_REDMPTN_STAT instead of
                                     PS_BPOM_MS_REDMPTN.BPOM_REDMPTN_TYPE in cursor cur_redmptn_info
  Chidamba            02/22/2013     g5 Incremental approach 
  Swati               05/29/2015     Bug 62484 - GoalQuest with Plateau - The GoalQuest with Plateau award 
                                     issued to a Participant is not getting displayed in any of the Plateau related reports
                      06/03/2015     Bug 62484 - GoalQuest with Plateau - The GoalQuest with Plateau award 
                                     issued to a Participant is not getting displayed in any of the Plateau related reports    
 Suresh J            06/10/2015      Bug 62484   
 nagarajs            11/11/2015      Bug 64499 - GoalQuest with Plateau - Plateau Item Selection - Participation by 
                                     Organization extract not extracting any data 
 nagarajs           12/16/2015       Bug 64989 - Plateau Item Selection - Participation by Organization extract not extracting
                                     award level and promotion name for recognition promotion with Plateau 
 nagarajs           01/07/2016       Bug 65373 - PKG_CLIENT_REPORTS using old function         
 nagarajs           02/09/2016       Bug 65619 - Plateau Levels Activity - Participation by Organization Showing wrong counts     
 nagarajs           02/18/2016       Bug 65816 - 'Plateau Levels Activity - Participation by Organization' report is not getting updated
 Sherif Basha       01/13/2017       Bug 70899 - P_AWARD_ORDER_DETAIL_REPORT query causing report refresh to fail 
************************************************************************************/

   PROCEDURE p_award_item_activity(p_in_user_id           IN  NUMBER,
                                   p_in_start_date        IN  DATE,
                                   p_in_end_date          IN  DATE,
                                   p_out_return_code      OUT NUMBER,
                                   p_out_error_message    OUT VARCHAR2)
   IS
      v_status       VARCHAR2 (1);
      v_exist_data   NUMBER;
      v_default_date DATE := SYSDATE;
      --Set 'R'-Redeemed, 'U'-Unredeemed, 'E'-Expired

      CURSOR cur_award_item_activity
      IS
         SELECT b.promotion_id, b.country_id, b.program_id,
                --fnc_cms_asset_code_value (b.cm_asset_key) om_level_name,
                om_level_name,      --01/27/2016
                fnc_java_decrypt (a.gift_code) mo_gift_code, a.order_number,
                a.merch_gift_code_type, a.expiration_date, a.is_redeemed,a.node_id,a.award_date,
                date_created       --02/09/2016 --date_modified --02/18/2016
           FROM (SELECT c.promotion_id, mo.order_number,
                        mo.promo_merch_program_level_id, mo.gift_code,
                        mo.merch_gift_code_type, mo.expiration_date,
                        mo.is_redeemed,
                        cr.node_id,
                        TRUNC(mo.date_created) award_date,
                        mo.date_created       --02/09/2016 --date_modified --02/18/2016
                   FROM merch_order mo, 
                        claim c, 
                        claim_item ci,
                        claim_recipient cr
                  WHERE mo.claim_id = c.claim_id(+)
                    AND c.claim_id  = ci.claim_id(+)            
                    AND cr.claim_item_id = ci.claim_item_id
                    AND (mo.date_created >= p_in_start_date AND mo.date_created <= p_in_end_date
                      OR mo.date_modified >= p_in_start_date AND mo.date_modified <= p_in_end_date)) a,
                (SELECT pmc.promotion_id, pmc.country_id, pmc.program_id,
                        pmcl.promo_merch_program_level_id, --pmcl.cm_asset_key, --01/27/2016
                        vw.cms_value om_level_name                              --01/27/2016
                   FROM promo_merch_country pmc,
                        promo_merch_program_level pmcl,
                        promotion p,
                        vw_cms_asset_value vw                                   --01/27/2016
                  WHERE p.promotion_id = pmc.promotion_id
                    AND vw.asset_code = pmcl.cm_asset_key                       --01/27/2016
                    AND vw.locale = 'en_US'                                     --01/27/2016
                    AND pmc.promo_merch_country_id =
                                                   pmcl.promo_merch_country_id
                    AND LOWER (p.promotion_type) = LOWER ('recognition')
                    AND LOWER (p.award_type) = LOWER ('merchandise')) b
          WHERE a.promo_merch_program_level_id = b.promo_merch_program_level_id
          UNION --05/29/2015 Bug 62484
          SELECT a.promotion_id,NULL country_id,NULL program_id,
                'Level '||GGL.sequence_num om_level_name,
                fnc_java_decrypt(mo.gift_code) mo_gift_code,mo.order_number, 
                mo.merch_gift_code_type,mo.expiration_date,mo.is_redeemed,un.node_id,
                TRUNC(mo.date_created) award_date,
                mo.date_created       --02/09/2016  --date_modified --02/18/2016
            FROM merch_order mo,ACTIVITY_MERCH_ORDER amo,
                activity a,promotion p,user_node un,goalquest_paxgoal gp,goalquest_goallevel ggl
            WHERE mo.merch_order_id = amo.merch_order_id
                AND amo.activity_id = a.activity_id
                AND p.promotion_id = a.promotion_id
                AND LOWER (p.promotion_type) = LOWER ('goalquest')
                AND LOWER (p.award_type) = LOWER ('merchandise')
                AND un.user_id = a.user_id
                AND un.is_primary = 1
                AND GP.PROMOTION_ID = a.promotion_id
                AND gp.user_id = a.user_id
                AND gp.goallevel_id = ggl.goallevel_id
                AND (trunc(mo.date_created) >= trunc(p_in_start_date) AND trunc(mo.date_created) <= trunc(p_in_end_date) --06/03/2015 Bug 62484  --06/10/2015
                      OR trunc(mo.date_modified) > trunc(p_in_start_date) AND trunc(mo.date_modified) <= trunc(p_in_end_date));  --06/10/2015

      CURSOR cur_total_issued
      IS
         SELECT   promotion_id, country_id, program_id, om_level_name,node_id,award_date,
                  COUNT (status) tot_count
             FROM tmp_award_item_activity_rpt
            WHERE mo_date_modified >= p_in_start_date       --02/18/2016
         GROUP BY promotion_id, country_id, program_id, om_level_name,node_id,award_date;

      CURSOR cur_total_status (p_status VARCHAR2)
      IS
         SELECT   promotion_id, country_id, program_id, om_level_name,node_id,award_date,
                  COUNT (1) stat_count
             FROM tmp_award_item_activity_rpt
            WHERE status = p_status
        GROUP BY promotion_id, country_id, program_id, om_level_name,node_id,award_date;
   BEGIN
    --INSERT_RPT_REFRESH_DATE('awarditemactivity', 'summary');
    --DELETE      rpt_award_item_activity;

      FOR rec_award_item_activity IN cur_award_item_activity
      LOOP
         --determines status of the order
         IF (rec_award_item_activity.mo_gift_code IS NOT NULL)
         THEN
         IF (LOWER (rec_award_item_activity.merch_gift_code_type) = 'product'
            )
         THEN
            IF (rec_award_item_activity.is_redeemed = 1)
            THEN
               v_status := 'R';
            ELSE
               v_status := 'U';
            END IF;
         ELSE
            --Future code will be here to check if merch_gift_code_type = 'level' is RI or IS status
            IF (rec_award_item_activity.is_redeemed = 1)
            THEN
               v_status := 'R';
            ELSE
               IF (NVL(rec_award_item_activity.expiration_date, TO_DATE('2999/12/31', 'YYYY/MM/DD')) > SYSDATE)
               THEN
                  v_status := 'U';
               ELSE
                  v_status := 'E';
               END IF;

            END IF;
         END IF;
         ELSE
         v_status := NULL;
         END IF;

            INSERT INTO tmp_award_item_activity_rpt
                        (promotion_id,
                         country_id,
                         program_id,
                         om_level_name,
                         gift_code, 
                         status,
                         node_id,
                         award_date,
                         mo_date_modified       --02/09/2016
                        )
                 VALUES (rec_award_item_activity.promotion_id,
                         rec_award_item_activity.country_id,
                         rec_award_item_activity.program_id,
                         rec_award_item_activity.om_level_name,
                         rec_award_item_activity.mo_gift_code, 
                         v_status,
                         rec_award_item_activity.node_id,
                         rec_award_item_activity.award_date,
                         rec_award_item_activity.date_created --02/09/2016 --date_modified --02/18/2016
                        );
      END LOOP;

      FOR rec_total_issued IN cur_total_issued
      LOOP
        
        BEGIN
           
          SELECT 1
            INTO v_exist_data 
            FROM rpt_award_item_activity
           WHERE NVL(program_id,1)    = NVL(rec_total_issued.program_id,1) --02/18/2016
             AND om_level_name = rec_total_issued.om_level_name 
             AND promotion_id  = rec_total_issued.promotion_id
             AND NVL(country_id,1)    = NVL(rec_total_issued.country_id,1) --02/18/2016
             AND NVL(node_id,0)                 = NVL(rec_total_issued.node_id,0)
             AND NVL(award_date,v_default_date) = NVL(rec_total_issued.award_date,v_default_date);  
            
          UPDATE rpt_award_item_activity
             SET tot_num_issued = (NVL(tot_num_issued,0)+rec_total_issued.tot_count),
                 date_modified  = SYSDATE,
                 modified_by    = p_in_user_id
           WHERE NVL(program_id,1)    = NVL(rec_total_issued.program_id,1) --05/29/2015 Bug 62484
             AND om_level_name = rec_total_issued.om_level_name
             AND promotion_id  = rec_total_issued.promotion_id
             AND NVL(country_id,1)    = NVL(rec_total_issued.country_id,1) --02/18/2016
             AND NVL(node_id,0)                    = NVL(rec_total_issued.node_id,0)
             AND NVL(award_date,v_default_date)    = NVL(rec_total_issued.award_date,v_default_date);  
        EXCEPTION 
         WHEN NO_DATA_FOUND THEN
          INSERT INTO rpt_award_item_activity(promotion_id,
                                              country_id,
                                              program_id,                                              
                                              om_level_name,
                                              node_id,
                                              award_date,
                                              tot_num_issued,
                                              tot_num_redeemed, 
                                              tot_num_unredeemed,
                                              tot_num_expired,
                                              date_created,
                                              created_by)
                                      VALUES (rec_total_issued.promotion_id,
                                              rec_total_issued.country_id,
                                              rec_total_issued.program_id,
                                              rec_total_issued.om_level_name,
                                              rec_total_issued.node_id,
                                              rec_total_issued.award_date,
                                              rec_total_issued.tot_count,
                                              NULL, 
                                              NULL,
                                              NULL,
                                              SYSDATE,
                                              p_in_user_id);
        END;
      END LOOP;

      FOR rec_total_issued IN cur_total_status ('E')
      LOOP
        UPDATE rpt_award_item_activity r
           SET tot_num_expired = NVL(tot_num_expired,0)+rec_total_issued.stat_count
         WHERE promotion_id  = rec_total_issued.promotion_id
           AND NVL(country_id,1)    = NVL(rec_total_issued.country_id,1) --05/29/2015 Bug 62484
           AND NVL(program_id,1)    = NVL(rec_total_issued.program_id,1) --05/29/2015 Bug 62484
           AND om_level_name = rec_total_issued.om_level_name
           AND NVL(node_id,0)                  = NVL(rec_total_issued.node_id,0)
           AND NVL(award_date,v_default_date)  = NVL(rec_total_issued.award_date,v_default_date);
           
        UPDATE rpt_award_item_activity                                   --02/09/2016 Bug 65619
            SET tot_num_unredeemed = tot_num_unredeemed - rec_total_issued.stat_count 
          WHERE promotion_id  = rec_total_issued.promotion_id
            AND NVL(country_id,1)    = NVL(rec_total_issued.country_id,1) 
            AND NVL(program_id,1)    = NVL(rec_total_issued.program_id,1) 
            AND om_level_name = rec_total_issued.om_level_name
            AND NVL(node_id,0)                 = NVL(rec_total_issued.node_id,0)
            AND NVL(award_date,v_default_date) = NVL(rec_total_issued.award_date,v_default_date);
      END LOOP;

      /*FOR rec_total_issued IN cur_total_status ('U') --02/18/2016
      LOOP
         UPDATE rpt_award_item_activity
            SET tot_num_unredeemed = NVL(tot_num_unredeemed,0)+rec_total_issued.stat_count
          WHERE promotion_id  = rec_total_issued.promotion_id
            AND NVL(country_id,1)    = NVL(rec_total_issued.country_id,1) --05/29/2015 Bug 62484
            AND NVL(program_id,1)    = NVL(rec_total_issued.program_id,1) --05/29/2015 Bug 62484
            AND om_level_name = rec_total_issued.om_level_name
            AND NVL(node_id,0)                 = NVL(rec_total_issued.node_id,0)
            AND NVL(award_date,v_default_date) = NVL(rec_total_issued.award_date,v_default_date);
      END LOOP;*/

      FOR rec_total_issued IN cur_total_status ('R')
      LOOP
         UPDATE rpt_award_item_activity
            SET tot_num_redeemed = NVL(tot_num_redeemed,0)+rec_total_issued.stat_count
          WHERE promotion_id = rec_total_issued.promotion_id
            AND NVL(country_id,1)    = NVL(rec_total_issued.country_id,1) --05/29/2015 Bug 62484
            AND NVL(program_id,1)    = NVL(rec_total_issued.program_id,1) --05/29/2015 Bug 62484
            AND om_level_name = rec_total_issued.om_level_name
            AND NVL(node_id,0)                 = NVL(rec_total_issued.node_id,0)
            AND NVL(award_date,v_default_date) = NVL(rec_total_issued.award_date,v_default_date);
            
          UPDATE rpt_award_item_activity                                   --02/09/2016 Bug 65619
            SET tot_num_unredeemed = tot_num_unredeemed - rec_total_issued.stat_count
          WHERE promotion_id  = rec_total_issued.promotion_id
            AND NVL(country_id,1)    = NVL(rec_total_issued.country_id,1) 
            AND NVL(program_id,1)    = NVL(rec_total_issued.program_id,1) 
            AND om_level_name = rec_total_issued.om_level_name
            AND NVL(node_id,0)                 = NVL(rec_total_issued.node_id,0)
            AND NVL(award_date,v_default_date) = NVL(rec_total_issued.award_date,v_default_date);
      END LOOP;
      
      FOR rec_total_issued IN cur_total_status ('U')  --02/18/2016
      LOOP
         UPDATE rpt_award_item_activity
            SET tot_num_unredeemed = NVL(tot_num_unredeemed,0)+rec_total_issued.stat_count
          WHERE promotion_id  = rec_total_issued.promotion_id
            AND NVL(country_id,1)    = NVL(rec_total_issued.country_id,1) --05/29/2015 Bug 62484
            AND NVL(program_id,1)    = NVL(rec_total_issued.program_id,1) --05/29/2015 Bug 62484
            AND om_level_name = rec_total_issued.om_level_name
            AND NVL(node_id,0)                 = NVL(rec_total_issued.node_id,0)
            AND NVL(award_date,v_default_date) = NVL(rec_total_issued.award_date,v_default_date);
      END LOOP;
      
      p_out_return_code:= 00;
   EXCEPTION
      WHEN OTHERS THEN
      p_out_return_code := 99;
         prc_execution_log_entry ('P_AWARD_ITEM_ACTIVITY',
                                   1,
                                  'ERROR',
                                  SQLERRM,
                                  NULL);
       p_out_error_message:= SQLERRM;
   END;

PROCEDURE P_AWARD_ITEM_SELECTION(p_in_user_id           IN  NUMBER,
                                 p_in_start_date        IN  DATE,
                                 p_in_end_date          IN  DATE,
                                 p_out_return_code      OUT NUMBER,
                                 p_out_error_message    OUT VARCHAR2) IS

  CURSOR cur_award_item_selection IS
   SELECT b.promotion_id, 
          b.promotion_name, 
          b.country_id, 
          b.country_name, 
          b.program_id, 
          --fnc_cms_asset_code_value (b.cm_asset_key) om_level_name,            --01/27/2016
          om_level_name,                                                        --01/27/2016
          a.reference_number, 
          fnc_java_decrypt (a.gift_code) mo_gift_code, 
          a.order_number,
          a.node_id,
          a.award_date
    FROM (SELECT c.promotion_id, 
                 p.promotion_name, 
                 mo.order_number, 
                 mo.promo_merch_program_level_id, 
                 mo.gift_code,
                 mo.merch_gift_code_type, 
                 mo.expiration_date, 
                 mo.is_redeemed, 
                 mo.reference_number,
                 c.node_id,
                 TRUNC(mo.date_created) award_date
            FROM merch_order mo, 
                 claim c, 
                 promotion p
           WHERE mo.claim_id = c.claim_id(+)
             AND c.promotion_id = p.promotion_id
             --AND (mo.date_created > p_in_start_date AND mo.date_created <= p_in_end_date --11/11/2015
              --OR mo.date_modified > p_in_start_date AND mo.date_modified <= p_in_end_date)  --11/11/2015
              ) a,
          (SELECT pmc.promotion_id, 
                  p.promotion_name, 
                  pmc.country_id, 
                  --fnc_cms_asset_code_value(con.cm_asset_code) country_name,   --01/27/2016
                  vw_cn.cms_value country_name,                                 --01/27/2016
                  pmc.program_id, 
                  pmcl.promo_merch_program_level_id, 
                  --pmcl.cm_asset_key                                           --01/27/2016
                  vw.cms_value om_level_name                                    --01/27/2016
             FROM promo_merch_country pmc,
                  promo_merch_program_level pmcl,
                  promotion p,
                  country con,
                  vw_cms_asset_value vw,                                        --01/27/2016
                  vw_cms_asset_value vw_cn                                      --01/27/2016
            WHERE p.PROMOTION_ID = pmc.promotion_id
              AND pmc.promo_merch_country_id = pmcl.promo_merch_country_id
              AND con.country_id = pmc.country_id
              AND vw.asset_code = pmcl.cm_asset_key                             --01/27/2016
              AND vw.locale = 'en_US'                                           --01/27/2016
              AND vw_cn.asset_code = con.cm_asset_code                          --01/27/2016
              AND vw_cn.locale = 'en_US'                                        --01/27/2016
              AND LOWER(p.PROMOTION_TYPE) = LOWER('recognition')
              AND LOWER(p.AWARD_TYPE) = LOWER('merchandise')) b
    WHERE a.promo_merch_program_level_id = b.promo_merch_program_level_id
      AND a.promotion_id = b.promotion_id
   UNION --05/29/2015 Bug 62484
   SELECT a.promotion_id,p.promotion_name,NULL country_id,NULL country_name,
    NULL program_id,
        'Level '||GGL.sequence_num om_level_name,mo.reference_number,
        fnc_java_decrypt(mo.gift_code) mo_gift_code,mo.order_number, 
        un.node_id,TRUNC(mo.date_created) award_date
    FROM merch_order mo,ACTIVITY_MERCH_ORDER amo,
        activity a,promotion p,user_node un,goalquest_paxgoal gp,goalquest_goallevel ggl
    WHERE mo.merch_order_id = amo.merch_order_id
        AND amo.activity_id = a.activity_id
        AND p.promotion_id = a.promotion_id
        AND LOWER (p.promotion_type) = LOWER ('goalquest')
        AND LOWER (p.award_type) = LOWER ('merchandise')
        AND un.user_id = a.user_id
        AND un.is_primary = 1
        AND GP.PROMOTION_ID = a.promotion_id
        AND gp.user_id = a.user_id
        AND gp.goallevel_id = ggl.goallevel_id;
        --AND (trunc(mo.date_created) >= p_in_start_date AND trunc(mo.date_created) <= p_in_end_date --06/03/2015 Bug 62484  --11/11/2015
              --OR trunc(mo.date_modified) > p_in_start_date AND trunc(mo.date_modified) <= p_in_end_date);  --11/11/2015
      
    CURSOR cur_product_selection(p_order_no IN VARCHAR2) IS
     SELECT PRODUCT_ID, DESCR,INV_ITEM_ID
       FROM PS_BPOM_MS_REDMPTN@MSMART.WORLD
      WHERE order_no = p_order_no
        AND bpom_redmptn_type = 'O';
        
    --TEMP variable until DB Link is made
    v_order_no          VARCHAR2(30);
    v_bpom_crt_status   VARCHAR2(6);
    
    v_product_id        NUMBER;
    v_item_name         VARCHAR2(30);
    
    v_count             NUMBER;
    
    v_promotion_id      tmp_award_item_selection_rpt.PROMOTION_ID%TYPE;
    v_country_id        tmp_award_item_selection_rpt.COUNTRY_ID%TYPE;
    v_program_id        tmp_award_item_selection_rpt.PROGRAM_ID%TYPE;
    v_om_level_name     tmp_award_item_selection_rpt.OM_LEVEL_NAME%TYPE;
    v_item              tmp_award_item_selection_rpt.ITEM_NAME%TYPE;
    v_reference_number  merch_order.reference_number%TYPE;
    
    BEGIN
        --INSERT_RPT_REFRESH_DATE('awarditemselection', 'summary');
        --DELETE RPT_AWARD_ITEM_SELECTION;
        FOR rec_award_item_selection IN cur_award_item_selection LOOP
            v_reference_number := rec_award_item_selection.reference_number;
            BEGIN
               SELECT ORDER_NO, BPOM_CRT_STATUS
                 INTO v_order_no, v_bpom_crt_status
               FROM  PS_BPOM_CRT_ORD_VW@MSMART.WORLD
               WHERE BPOM_CRT_XREF_NBR = rec_award_item_selection.reference_number
                 AND BPOM_ORD_STATUS = 'W'; 
            EXCEPTION
               WHEN OTHERS THEN
                  v_order_no := NULL;
                  v_bpom_crt_status := NULL;
            END;
            
            IF v_order_no IS NOT NULL AND v_bpom_crt_status IN ('IS','RI') THEN
                FOR rec_product_selection IN cur_product_selection(v_order_no) LOOP
                    INSERT INTO TMP_AWARD_ITEM_SELECTION_RPT(promotion_id, promotion_name, 
                                                             country_id, country_name, 
                                                             program_id, om_level_name, 
                                                             order_number, product_id, 
                                                             item_name,award_date,
                                                             node_id,inv_item_id)
                    VALUES (rec_award_item_selection.promotion_id, rec_award_item_selection.promotion_name, 
                            rec_award_item_selection.country_id, rec_award_item_selection.country_name, 
                            rec_award_item_selection.program_id, rec_award_item_selection.om_level_name, 
                            rec_award_item_selection.order_number, rec_product_selection.product_id, 
                            rec_product_selection.DESCR,rec_award_item_selection.award_date,
                            rec_award_item_selection.node_id, rec_product_selection.inv_item_id);
                END LOOP;
            END IF;
        END LOOP;
        
        
      MERGE INTO rpt_award_item_selection rpt
        USING (        
        SELECT tmp.promotion_id, tmp.promotion_name, 
                      tmp.country_id, tmp.country_name, 
                      tmp.program_id, tmp.om_level_name, 
                      tmp.item_name,  tmp.award_date,
                       tmp.inv_item_id,
                      tmp.node_id, count(1) item_count
             FROM tmp_award_item_selection_rpt tmp
--                      (SELECT promotion_id, 
--                              country_id, 
--                              program_id, 
--                              om_level_name,
--                              award_date,
--                              inv_item_id,
--                              node_id,
--                              count(1) level_count
--                         FROM tmp_award_item_selection_rpt
--                     GROUP BY promotion_id, promotion_name, 
--                              country_id, country_name, 
--                              program_id, om_level_name,node_id,
--                              award_date,inv_item_id) lev
--              WHERE tmp.promotion_id = lev.promotion_id
--                AND tmp.country_id = lev.country_id
--                AND tmp.program_id = lev.program_id
--                AND tmp.om_level_name = lev.om_level_name                                                  
           GROUP BY tmp.promotion_id, 
                    tmp.promotion_name, 
                    tmp.country_id, 
                    tmp.country_name, 
                    tmp.program_id, 
                    tmp.om_level_name, 
                    tmp.item_name,
                    tmp.award_date,
                    tmp.inv_item_id,                     
                    tmp.node_id                                        
                    ) e
        ON ( rpt.promotion_id       = e.promotion_id  
         AND NVL(rpt.country_id,1)  = NVL(e.country_id,1) --05/29/2015 Bug 62484
         AND NVL(rpt.program_id,1)  = NVL(e.program_id,1) --05/29/2015 Bug 62484
         AND rpt.om_level_name      = e.om_level_name
         AND rpt.item_name          = e.item_name
         AND rpt.inv_item_id        = e.inv_item_id  
         AND rpt.award_date         = e.award_date
         AND rpt.node_id            = e.node_id)
        WHEN MATCHED THEN
          UPDATE 
             SET 
                 rpt.item_count         = e.item_count, --rpt.item_count + e.item_count, --11/11/2015
                 rpt.country_name       = e.country_name,
                 rpt.promotion_name     = e.promotion_name,
                 rpt.date_modified      = SYSDATE,
                 rpt.modified_by        = P_IN_USER_ID
           WHERE rpt.promotion_id       = e.promotion_id  
             AND NVL(rpt.country_id,1)  = NVL(e.country_id,1) --05/29/2015 Bug 62484
             AND NVL(rpt.program_id,1)  = NVL(e.program_id,1) --05/29/2015 Bug 62484
             AND rpt.om_level_name      = e.om_level_name
             AND rpt.item_name          = e.item_name
             AND rpt.inv_item_id        = e.inv_item_id  
             AND rpt.award_date         = e.award_date
             AND rpt.node_id            = e.node_id
        WHEN NOT MATCHED THEN
           INSERT (promotion_id,
                  promotion_name,
                  country_id,
                  country_name,
                  program_id,
                  om_level_name,
                  item_name,
                  inv_item_id,                  
                  item_count,
                  award_date,
                  node_id,
                  created_by,
                  date_created)
           VALUES(e.promotion_id, 
                  e.promotion_name, 
                  e.country_id, 
                  e.country_name, 
                  e.program_id, 
                  e.om_level_name, 
                  e.item_name,
                  e.inv_item_id,                      
                  e.item_count,
                  e.award_date,
                  e.node_id,
                  P_IN_USER_ID,
                  SYSDATE);         
        
--        MERGE INTO rpt_award_item_selection rpt
--        USING (SELECT tmp.promotion_id, tmp.promotion_name, 
--                      tmp.country_id, tmp.country_name, 
--                      tmp.program_id, tmp.om_level_name, 
--                      tmp.item_name,  tmp.award_date,
--                      lev.level_count, tmp.inv_item_id,
--                      tmp.node_id, count(1) item_count
--                 FROM tmp_award_item_selection_rpt tmp,
--                      (SELECT promotion_id, 
--                              country_id, 
--                              program_id, 
--                              om_level_name,
--                              award_date,
--                              inv_item_id,
--                              node_id,
--                              count(1) level_count
--                         FROM tmp_award_item_selection_rpt
--                     GROUP BY promotion_id, promotion_name, 
--                              country_id, country_name, 
--                              program_id, om_level_name,node_id,
--                              award_date,inv_item_id) lev
--              WHERE tmp.promotion_id = lev.promotion_id
--                AND tmp.country_id = lev.country_id
--                AND tmp.program_id = lev.program_id
--                AND tmp.om_level_name = lev.om_level_name                                                  
--           GROUP BY tmp.promotion_id, 
--                    tmp.promotion_name, 
--                    tmp.country_id, 
--                    tmp.country_name, 
--                    tmp.program_id, 
--                    tmp.om_level_name, 
--                    tmp.item_name,
--                    tmp.award_date,
--                    tmp.inv_item_id, 
--                    lev.level_count,
--                    tmp.node_id) e
--        ON ( rpt.promotion_id     = e.promotion_id  
--         AND rpt.country_id       = e.country_id
--         AND rpt.program_id       = e.program_id
--         AND rpt.om_level_name    = e.om_level_name
--         AND rpt.item_name        = e.item_name
--         AND rpt.inv_item_id      = e.inv_item_id  
--         AND rpt.award_date       = e.award_date
--         AND rpt.node_id          = e.node_id)
--        WHEN MATCHED THEN
--          UPDATE 
--             SET rpt.level_count   = rpt.level_count + e.level_count,
--                 rpt.item_count    = rpt.item_count + e.item_count,
--                 rpt.country_name  = e.country_name,
--                 rpt.promotion_name = e.promotion_name,
--                 rpt.date_modified = SYSDATE,
--                 rpt.modified_by   = p_in_user_id
--           WHERE rpt.promotion_id     = e.promotion_id  
--             AND rpt.country_id       = e.country_id
--             AND rpt.program_id       = e.program_id
--             AND rpt.om_level_name    = e.om_level_name
--             AND rpt.item_name        = e.item_name
--             AND rpt.inv_item_id      = e.inv_item_id  
--             AND rpt.award_date       = e.award_date
--             AND rpt.node_id          = e.node_id
--        WHEN NOT MATCHED THEN
--           INSERT (promotion_id,
--                  promotion_name,
--                  country_id,
--                  country_name,
--                  program_id,
--                  om_level_name,
--                  item_name,
--                  inv_item_id,
--                  level_count,                  
--                  item_count,
--                  award_date,
--                  node_id,
--                  created_by,
--                  date_created)
--           VALUES(e.promotion_id, 
--                  e.promotion_name, 
--                  e.country_id, 
--                  e.country_name, 
--                  e.program_id, 
--                  e.om_level_name, 
--                  e.item_name,
--                  e.inv_item_id,
--                  e.level_count,                   
--                  e.item_count,
--                  e.award_date,
--                  e.node_id,
--                  p_in_user_id,
--                  SYSDATE);           
                   
    EXCEPTION
      WHEN OTHERS THEN
         prc_execution_log_entry ('P_AWARD_ITEM_SELECTION',
                                   1,
                                  'ERROR',
                                  'Failed on reference number '||v_reference_number||' '||SQLERRM,
                                  NULL);
                       
    END;
    
PROCEDURE P_AWARD_ORDER_DETAIL_REPORT IS

    CURSOR cur_award_order_detail IS
    SELECT p.promotion_name promotion_name,
           con.country_code country_code, 
       mo.reference_number reference_number,
       --fnc_cms_asset_code_value (pmcl.cm_asset_key) om_level_name,--01/27/2016
       vw.cms_value om_level_name,                                  --01/27/2016
       mo.DATE_CREATED date_issued,
       NULL date_redeemed,
       NULL date_cancelled,
       NULL order_number,
       NULL item_description,
       au.user_id,              --ADDED on 05/22/2009 
       au.user_name employee_id,
       au.FIRST_NAME,
       au.MIDDLE_NAME,
       au.LAST_NAME,
       ua.ADDR1,
       ua.addr2,
       ua.CITY,
       ua.state,
       ua.POSTAL_CODE,
       NULL hire_date,
       NULL ship_to_first_name,
       NULL ship_to_last_name,
       NULL ship_to_addr1,
       NULL ship_to_addr2,
       NULL ship_to_city,
       NULL ship_to_state,
       NULL ship_to_postal_code                                   
    FROM merch_order mo,
       claim       c,
       PROMO_MERCH_COUNTRY pmc,
       PROMO_MERCH_PROGRAM_LEVEL pmcl,
       APPLICATION_USER          au,
       USER_ADDRESS              ua,
       promotion                 p,
       country                   con,
       vw_cms_asset_value        vw                                   --01/27/2016
    WHERE mo.claim_id = c.claim_id(+)
        AND mo.participant_id = au.USER_ID
        AND au.user_id = ua.user_id
        AND ua.is_primary = 1
        AND pmc.promotion_id = NVL(c.promotion_id, pmc.promotion_id)
        AND pmc.promo_merch_country_id = pmcl.promo_merch_country_id
        AND mo.promo_merch_program_level_id = pmcl.promo_merch_program_level_id 
        AND pmc.COUNTRY_ID = con.COUNTRY_ID
        AND c.PROMOTION_ID = p.PROMOTION_ID  
        AND vw.asset_code = pmcl.cm_asset_key                       --01/27/2016
        AND vw.locale = 'en_US'                                     --01/27/2016
    UNION
    SELECT p.promotion_name promotion_name,
           con.country_code country_code,     
       mo.reference_number reference_number,
       --fnc_cms_asset_code_value (pmcl.cm_asset_key) om_level_name,--01/27/2016
       vw.cms_value om_level_name,                                  --01/27/2016
       mo.DATE_CREATED date_issued,
       NULL date_redeemed,
       NULL date_cancelled,
       NULL order_number,
       NULL item_description,
       NULL user_id,                --ADDED on 05/22/2009 
       NULL employee_id,
       NULL,
       NULL MIDDLE_NAME,
       NULL,
       NULL addr1,
       NULL addr2,
       NULL CITY,
       NULL state,
       NULL POSTAL_CODE,
       NULL hire_date,
       NULL ship_to_first_name,
       NULL ship_to_last_name,
       NULL ship_to_addr1,
       NULL ship_to_addr2,
       NULL ship_to_city,
       NULL ship_to_state,
       NULL ship_to_postal_code                                   
    FROM merch_order mo,
       claim       c,
       PROMO_MERCH_COUNTRY pmc,
       PROMO_MERCH_PROGRAM_LEVEL pmcl,
       promotion                 p,
       country                   con,
       vw_cms_asset_value        vw                                   --01/27/2016       
    WHERE mo.claim_id = c.claim_id(+)
        AND mo.participant_id IS NULL
        AND pmc.promotion_id = NVL(c.promotion_id, pmc.promotion_id)
        AND pmc.promo_merch_country_id = pmcl.promo_merch_country_id
        AND mo.promo_merch_program_level_id = pmcl.promo_merch_program_level_id
        AND pmc.COUNTRY_ID = con.COUNTRY_ID
        AND c.PROMOTION_ID = p.PROMOTION_ID
        AND vw.asset_code = pmcl.cm_asset_key                       --01/27/2016
        AND vw.locale = 'en_US';                                    --01/27/2016
        
    CURSOR cur_redmptn_info(p_order_no IN VARCHAR2)IS        
    SELECT order_no, line_no, product_id, descr
      FROM PS_BPOM_MS_REDMPTN@MSMART.WORLD
     WHERE PS_BPOM_MS_REDMPTN.ORDER_NO = p_order_no
       --AND PS_BPOM_MS_REDMPTN.BPOM_REDMPTN_TYPE = 'O'     --07/22/2011, Bug # 37598 fix
       AND PS_BPOM_MS_REDMPTN.BPOM_REDMPTN_STAT = 'O'
     MINUS
    SELECT order_no, line_no, product_id, descr 
      FROM PS_BPOM_MS_REDMPTN@MSMART.WORLD
     WHERE PS_BPOM_MS_REDMPTN.ORDER_NO = p_order_no  
       --AND PS_BPOM_MS_REDMPTN.BPOM_REDMPTN_TYPE = 'X'     --07/22/2011, Bug # 37598 fix
       AND PS_BPOM_MS_REDMPTN.BPOM_REDMPTN_STAT = 'X';    
        
    v_order_no             VARCHAR2(30);
    v_bpom_crt_status      VARCHAR2(6);
    
    v_product_id           NUMBER;
    v_item_name            VARCHAR2(30);
    v_date_redeemed        DATE;
    v_date_cancelled       DATE;
    v_bpom_cert_stat_dttm  DATE;
    v_item_description     RPT_AWARD_ORDER_DETAIL.ITEM_DESCRIPTION%TYPE;
    v_employee_id          RPT_AWARD_ORDER_DETAIL.EMPLOYEE_ID%TYPE;
    v_hire_date            RPT_AWARD_ORDER_DETAIL.hire_date%TYPE;
    v_ship_to_first_name   RPT_AWARD_ORDER_DETAIL.SHIP_TO_CNTCT_NAME1%TYPE;
    v_ship_to_address_1    RPT_AWARD_ORDER_DETAIL.SHIP_TO_ADDRESS_1%TYPE;
    v_ship_to_address_2    RPT_AWARD_ORDER_DETAIL.SHIP_TO_ADDRESS_2%TYPE;
    v_ship_to_city         RPT_AWARD_ORDER_DETAIL.SHIP_TO_CITY%TYPE;
    v_ship_to_state        RPT_AWARD_ORDER_DETAIL.SHIP_TO_STATE%TYPE;
    v_ship_to_postal_code  RPT_AWARD_ORDER_DETAIL.SHIP_TO_POSTAL_CODE%TYPE;
    v_reference_number     merch_order.reference_number%TYPE;
        
    BEGIN

     --INSERT_RPT_REFRESH_DATE('awardorder', 'detail');
       DELETE RPT_AWARD_ORDER_DETAIL;
        
        FOR rec_award_order_detail IN cur_award_order_detail LOOP
            v_reference_number := rec_award_order_detail.reference_number;
            BEGIN
               SELECT ORDER_NO, BPOM_CRT_STATUS, BPOM_CRT_STAT_DTTM
                 INTO v_order_no, v_bpom_crt_status, v_bpom_cert_stat_dttm
                 FROM PS_BPOM_CRT_ORD_VW@MSMART.WORLD
                WHERE BPOM_CRT_XREF_NBR = rec_award_order_detail.reference_number
                  AND BPOM_ORD_STATUS = 'W';
            EXCEPTION
               WHEN OTHERS THEN
                  v_order_no := NULL;
                  v_bpom_crt_status := NULL;
                  v_bpom_cert_stat_dttm := NULL;
            END;
            
            IF UPPER(v_bpom_crt_status) = 'CN' THEN
                v_date_cancelled := v_bpom_cert_stat_dttm;    
            ELSE
                v_date_cancelled := NULL;
            END IF;        
                
            IF(v_order_no IS NOT NULL) AND v_bpom_crt_status IN ('IS','RI','CN') THEN 
            
               FOR rec_redmptn_info IN cur_redmptn_info(v_order_no) LOOP   
                  BEGIN
                     SELECT PS_BPOM_MS_REDMPTN.PROCESS_DTTM, DESCR 
                       INTO v_date_redeemed, v_item_description
                       FROM PS_BPOM_MS_REDMPTN@MSMART.WORLD
                      WHERE ORDER_NO = rec_redmptn_info.order_no
                        AND LINE_NO  = rec_redmptn_info.line_no
                        AND PS_BPOM_MS_REDMPTN.BPOM_REDMPTN_TYPE = 'O'
                        AND PS_BPOM_MS_REDMPTN.BPOM_REDMPTN_STAT <> 'X';    -- 01/13/2017 Bug 70899
                  EXCEPTION
                     WHEN NO_DATA_FOUND THEN
                        v_date_redeemed := NULL;
                        v_item_description  := NULL;               
                   END;           

                  v_employee_id := rec_award_order_detail.employee_id;
                  
                  BEGIN
                     SELECT CNTCT_NAME1, ADDRESS1, ADDRESS2, CITY, STATE, POSTAL
                       INTO v_ship_to_first_name, v_ship_to_address_1, v_ship_to_address_2,
                            v_ship_to_city, v_ship_to_state, v_ship_to_postal_code
                       FROM PS_BPOM_MS_SHIP@MSMART.WORLD
                      WHERE ORDER_NO           = rec_redmptn_info.order_no
                        AND ORDER_INT_LINE_NO  = rec_redmptn_info.line_no
                        AND ROWNUM = 1;         --added on 04/20/2010  
                  EXCEPTION
                     WHEN NO_DATA_FOUND THEN
                        v_ship_to_first_name := NULL;
                        v_ship_to_address_1  := NULL;
                        v_ship_to_address_2  := NULL;
                        v_ship_to_city       := NULL;
                        v_ship_to_state      := NULL;
                        v_ship_to_postal_code := NULL;                     
                  END;                                    
                                    
                  BEGIN                             
                    -- SELECT hire_date   --MAX(hire_date)        --ADDED on 05/22/2009 
                      -- INTO v_hire_date
                      -- FROM PARTICIPANT_EMPLOYER
                    --  WHERE user_id = rec_award_order_detail.user_id
                    --    AND termination_date is NULL;
                    SELECT MAX(hire_date) --Added on Bug # 26850
                        INTO v_hire_date
                        FROM application_user a, PARTICIPANT_EMPLOYER e
                    WHERE e.termination_date is NULL
                        AND a.user_id = e.user_id   
                        AND a.user_name = rec_award_order_detail.employee_id; 
                  EXCEPTION
                    WHEN OTHERS THEN
                       v_hire_date:= NULL;
                  END;
                       
                  INSERT INTO RPT_AWARD_ORDER_DETAIL
                          (PROMOTION_NAME,
                          COUNTRY_CODE,
                          REFERENCE_NUMBER,
                          AWARD_LEVEL,
                          DATE_ISSUED,
                          DATE_REDEEMED,
                          DATE_CANCELLED,
                          ORDER_NUMBER,
                          ITEM_DESCRIPTION,
                          EMPLOYEE_ID,
                          FIRST_NAME,
                          MIDDLE_NAME,
                          LAST_NAME,
                          ADDRESS_1,
                          ADDRESS_2,
                          CITY,
                          STATE,
                          POSTAL_CODE,
                          HIRE_DATE,
                          SHIP_TO_CNTCT_NAME1,
                          SHIP_TO_ADDRESS_1,
                          SHIP_TO_ADDRESS_2,
                          SHIP_TO_CITY,
                          SHIP_TO_STATE,
                          SHIP_TO_POSTAL_CODE)
                          VALUES
                          (rec_award_order_detail.promotion_name,
                          rec_award_order_detail.country_code,
                          rec_award_order_detail.REFERENCE_NUMBER,
                          rec_award_order_detail.om_level_name,
                          rec_award_order_detail.DATE_ISSUED,
                          v_date_redeemed,
                          v_date_cancelled,
                          rec_redmptn_info.order_no,
                          v_item_description,
                          v_employee_id,
                          rec_award_order_detail.FIRST_NAME,
                          rec_award_order_detail.MIDDLE_NAME,
                          rec_award_order_detail.LAST_NAME,
                          rec_award_order_detail.ADDR1,
                          rec_award_order_detail.ADDR2,
                          rec_award_order_detail.CITY,
                          rec_award_order_detail.STATE,
                          rec_award_order_detail.POSTAL_CODE,
                          v_hire_date,
                          v_ship_to_first_name,
                          v_ship_to_address_1,
                          v_ship_to_address_2,
                          v_ship_to_city,
                          v_ship_to_state,
                          v_ship_to_postal_code);
               END LOOP;                        
            END IF;
        END LOOP;
    EXCEPTION
      WHEN OTHERS THEN
         prc_execution_log_entry ('P_AWARD_ORDER_DETAIL_REPORT',
                                   1,
                                  'ERROR',
                                  'Failed on reference number '||v_reference_number||' '||SQLERRM,
                                  NULL);
        
    END;  -- End of procedure P_AWARD_ORDER_DETAIL_REPORT
    
    
    PROCEDURE P_AWARD_EARNINGS_REPORT IS
    
       CURSOR cur_award_earnings IS
       SELECT  c.USER_ID, c.LAST_NAME, c.FIRST_NAME, c.SSN, --fnc_cms_asset_code_value (b.cm_asset_key) AWARD_LEVEL, --01/27/2016
                b.award_level,  --01/27/2016
                a.process_dttm DATE_REDEEMED, a.points MAX_VALUE_LEVEL, a.points ITEM_VALUE, a.reference_number, b.program_id PROGRAM_NUMBER, a.DESCR, a.date_created DATE_ISSUED, c.EMAIL_ADDR, c.ADDR1, c.ADDR2, c.ADDR3, c.CITY, c.STATE, c.POSTAL_CODE, c.country_code country, c.USER_NAME, NVL(a.promotion_id,b.promotion_id) promotion_id, c.country_id --11/11/2015 gt promotion id from a instaed of b alias  --12/16/2015 added NVL to promotion id
          FROM (SELECT mo.participant_id, 
                       mo.promo_merch_program_level_id,
                       mo.REFERENCE_NUMBER, mo.IS_REDEEMED, mo.DATE_CREATED, m.PROCESS_DTTM, mo.POINTS, m.DESCR,
                       a.promotion_id --11/11/2015
                  FROM merch_order mo, 
                       claim c, 
                       activity_merch_order amo,  --11/11/2015
                       activity a,                --11/11/2015                 
                       (SELECT mr.PROCESS_DTTM, cov.BPOM_CRT_XREF_NBR, mr.line_no, mr.DESCR FROM PS_BPOM_CRT_ORD_VW@MSMART.WORLD cov, PS_BPOM_MS_REDMPTN@MSMART.WORLD mr WHERE cov.order_no = mr.order_no AND mr.BPOM_REDMPTN_TYPE = 'O' AND cov.BPOM_ORD_STATUS = 'W' MINUS SELECT mr.PROCESS_DTTM, cov.BPOM_CRT_XREF_NBR, mr.line_no, mr.DESCR FROM PS_BPOM_CRT_ORD_VW@MSMART.WORLD cov, PS_BPOM_MS_REDMPTN@MSMART.WORLD mr WHERE cov.order_no = mr.order_no AND mr.BPOM_REDMPTN_TYPE = 'X' AND cov.BPOM_ORD_STATUS = 'W') m
                 WHERE mo.claim_id = c.claim_id(+)
                   AND mo.merch_order_id = amo.merch_order_id(+)   --11/11/2015 --12/16/2015
                   AND amo.activity_id = a.activity_id(+)          --11/11/2015 --12/16/2015     
                   AND mo.REFERENCE_NUMBER = m.BPOM_CRT_XREF_NBR) a,
               (SELECT pmcl.promo_merch_program_level_id, pmcl.PROGRAM_ID, pmcl.CM_ASSET_KEY, p.PROMOTION_ID,
                       vw.cms_value award_level                              --01/27/2016
                  FROM promo_merch_country pmc,
                       promo_merch_program_level pmcl,
                       promotion p,
                       vw_cms_asset_value vw                                   --01/27/2016
                 WHERE p.promotion_id = pmc.promotion_id
                   AND pmc.promo_merch_country_id = pmcl.promo_merch_country_id
                   AND vw.asset_code = pmcl.cm_asset_key                       --01/27/2016
                   AND vw.locale = 'en_US'                                     --01/27/2016
--                   AND LOWER (p.promotion_type) = LOWER ('recognition')  --06/10/2015
                   AND LOWER (p.promotion_type) IN ('merchandise','goalquest','recognition')  --06/10/2015 --12/16/2015 added recognition promotion type
                   AND LOWER (p.award_type) = LOWER ('merchandise')    
                   ) b,
               (SELECT au.USER_ID, au.LAST_NAME, au.FIRST_NAME, au.SSN, em.EMAIL_ADDR, ua.ADDR1, ua.ADDR2, ua.ADDR3, ua.CITY, ua.STATE, ua.POSTAL_CODE, co.country_code, au.USER_NAME, co.country_id
                 FROM application_user au,
                      (SELECT uea.USER_ID, uea.EMAIL_ADDR FROM user_email_address uea WHERE uea.IS_PRIMARY = 1) em,
                      user_address ua,
                      country co
                 WHERE au.user_id = em.user_id(+) 
                   AND au.user_id = ua.user_id
                   AND ua.COUNTRY_ID = co.COUNTRY_ID
                   AND ua.IS_PRIMARY = 1) c
         WHERE a.participant_id = c.USER_ID
--           AND a.promo_merch_program_level_id(+) = b.promo_merch_program_level_id;  --06/10/2015
           AND a.promo_merch_program_level_id = b.promo_merch_program_level_id (+);   --06/10/2015             
          
           
           TYPE temp_char_value_table IS TABLE OF user_characteristic.CHARACTERISTIC_VALUE%TYPE;

           -- Define a variable of the associative array type.
           user_characteristic_value temp_char_value_table:=temp_char_value_table();
           
           rpt_award_earnings_pk  NUMBER;
           v_reference_number     merch_order.reference_number%TYPE;
           
     BEGIN
          
            --INSERT_RPT_REFRESH_DATE('awardorder', 'earnings');
            DELETE RPT_AWARD_EARNINGS;
                     
          FOR rec_award_earnings IN cur_award_earnings LOOP
            v_reference_number := rec_award_earnings.reference_number; 
            SELECT rpt_award_earnings_pk_sq.nextval
                INTO rpt_award_earnings_pk
            FROM dual;
          
            SELECT characteristic_value
                BULK COLLECT INTO user_characteristic_value
            FROM user_characteristic uc
            WHERE uc.USER_ID = rec_award_earnings.user_id;

            user_characteristic_value.extend(15);
            INSERT INTO RPT_AWARD_EARNINGS (RPT_AWARD_EARNINGS_ID,
                                            LAST_NAME, 
                                            FIRST_NAME, 
                                            PROMOTION_ID, 
                                            AWARD_LEVEL, 
                                            REFERENCE_NUMBER, 
                                            PROGRAM_NUMBER, 
                                            MAX_VALUE_LEVEL, 
                                            ITEM_VALUE, 
                                            DATE_REDEEMED, 
                                            DATE_ISSUED, 
                                            ITEM_REDEEMED, 
                                            EMAIL_ADDRESS, 
                                            SSN, 
                                            ADDR1, 
                                            ADDR2, 
                                            ADDR3, 
                                            CITY, 
                                            STATE, 
                                            ZIP, 
                                            COUNTRY_ID, 
                                            COUNTRY, 
                                            LOGIN_ID, 
                                            CHARACTERISTIC_VALUE1, 
                                            CHARACTERISTIC_VALUE2, 
                                            CHARACTERISTIC_VALUE3, 
                                            CHARACTERISTIC_VALUE4, 
                                            CHARACTERISTIC_VALUE5,
                                            CHARACTERISTIC_VALUE6,
                                            CHARACTERISTIC_VALUE7,
                                            CHARACTERISTIC_VALUE8,
                                            CHARACTERISTIC_VALUE9,
                                            CHARACTERISTIC_VALUE10,
                                            CHARACTERISTIC_VALUE11,
                                            CHARACTERISTIC_VALUE12,
                                            CHARACTERISTIC_VALUE13,
                                            CHARACTERISTIC_VALUE14,
                                            CHARACTERISTIC_VALUE15)
                                           VALUES
                                           (rpt_award_earnings_pk, 
                                           rec_award_earnings.LAST_NAME, 
                                           rec_award_earnings.FIRST_NAME, 
                                           rec_award_earnings.promotion_id, 
                                           rec_award_earnings.AWARD_LEVEL, 
                                           rec_award_earnings.REFERENCE_NUMBER, 
                                           rec_award_earnings.PROGRAM_NUMBER, 
                                           rec_award_earnings.MAX_VALUE_LEVEL, 
                                           rec_award_earnings.ITEM_VALUE, 
                                           rec_award_earnings.DATE_REDEEMED, 
                                           rec_award_earnings.DATE_ISSUED, 
                                           rec_award_earnings.DESCR, 
                                           rec_award_earnings.EMAIL_ADDR, 
                                           rec_award_earnings.SSN, 
                                           rec_award_earnings.ADDR1, 
                                           rec_award_earnings.ADDR2, 
                                           rec_award_earnings.ADDR3, 
                                           rec_award_earnings.CITY, 
                                           rec_award_earnings.STATE, 
                                           rec_award_earnings.postal_code, 
                                           rec_award_earnings.country_id, 
                                           rec_award_earnings.COUNTRY, 
                                           rec_award_earnings.user_name, 
                                           user_characteristic_value(1), 
                                           user_characteristic_value(2), 
                                           user_characteristic_value(3), 
                                           user_characteristic_value(4), 
                                           user_characteristic_value(5),
                                           user_characteristic_value(6), 
                                           user_characteristic_value(7), 
                                           user_characteristic_value(8), 
                                           user_characteristic_value(9), 
                                           user_characteristic_value(10),
                                           user_characteristic_value(11), 
                                           user_characteristic_value(12), 
                                           user_characteristic_value(13), 
                                           user_characteristic_value(14), 
                                           user_characteristic_value(15));
          
          END LOOP;
     EXCEPTION
       WHEN OTHERS THEN
         prc_execution_log_entry ('P_AWARD_EARNINGS_REPORT',
                                   1,
                                  'ERROR',
                                  'Failed on reference number '||v_reference_number||' '||SQLERRM,
                                  NULL);
         
     END;  -- End of procedure P_AWARD_EARNINGS_REPORT

   PROCEDURE p_award_item_activity_sa( p_in_user_id           IN  NUMBER,
                                       p_in_start_date        IN  DATE,
                                       p_in_end_date          IN  DATE,
                                       p_out_return_code      OUT NUMBER,
                                       p_out_error_message    OUT VARCHAR2)
   IS
      v_status       VARCHAR2 (1);
      v_exist_data   NUMBER;
      v_default_date DATE := SYSDATE;

      CURSOR cur_award_item_activity
      IS
         SELECT b.promotion_id, b.country_id, b.program_id,
                om_level_name,      
                fnc_java_decrypt (a.gift_code) mo_gift_code, a.order_number,
                a.merch_gift_code_type, a.expiration_date, a.is_redeemed,a.node_id,a.award_date,
                date_created       
           FROM (SELECT c.promotion_id, mo.order_number,
                        mo.promo_merch_program_level_id, mo.gift_code,
                        mo.merch_gift_code_type, mo.expiration_date,
                        mo.is_redeemed,
                        cr.node_id,
                        TRUNC(mo.date_created) award_date,
                        mo.date_created       
                   FROM merch_order mo, 
                        claim c, 
                        claim_item ci,
                        claim_recipient cr
                  WHERE mo.claim_id = c.claim_id(+)
                    AND c.claim_id  = ci.claim_id(+)            
                    AND cr.claim_item_id = ci.claim_item_id
                    AND (mo.date_created >= p_in_start_date AND mo.date_created <= p_in_end_date
                      OR mo.date_modified >= p_in_start_date AND mo.date_modified <= p_in_end_date)) a,
                (SELECT pmc.promotion_id, pmc.country_id, pmc.program_id,
                        pmcl.promo_merch_program_level_id, 
                        vw.cms_value om_level_name         
                   FROM promo_merch_country pmc,
                        promo_merch_program_level pmcl,
                        promotion p,
                        vw_cms_asset_value vw              
                  WHERE p.promotion_id = pmc.promotion_id
                    AND NOT EXISTS (SELECT r.promotion_id  
                                                FROM promo_recognition r,
                                                     promotion pp   
                                                WHERE r.promotion_id = pp.promotion_id AND
                                                      r.promotion_id = p.promotion_id  AND
                                                      --pp.award_type = 'merchandise' AND 
                                                      (r.is_include_purl = 1 OR r.include_celebrations = 1))            --Exclude PURL since SA replaces PURL
                    AND vw.asset_code = pmcl.cm_asset_key                       
                    AND vw.locale = 'en_US'                                     
                    AND pmc.promo_merch_country_id =
                                                   pmcl.promo_merch_country_id
                    AND LOWER (p.promotion_type) = LOWER ('recognition')
                    AND LOWER (p.award_type) = LOWER ('merchandise')) b
          WHERE a.promo_merch_program_level_id = b.promo_merch_program_level_id
          UNION --05/29/2015 Bug 62484
          SELECT a.promotion_id,NULL country_id,NULL program_id,
                'Level '||GGL.sequence_num om_level_name,
                fnc_java_decrypt(mo.gift_code) mo_gift_code,mo.order_number, 
                mo.merch_gift_code_type,mo.expiration_date,mo.is_redeemed,un.node_id,
                TRUNC(mo.date_created) award_date,
                mo.date_created       --02/09/2016  --date_modified --02/18/2016
            FROM merch_order mo,ACTIVITY_MERCH_ORDER amo,
                activity a,promotion p,user_node un,goalquest_paxgoal gp,goalquest_goallevel ggl
            WHERE mo.merch_order_id = amo.merch_order_id
                AND amo.activity_id = a.activity_id
                AND p.promotion_id = a.promotion_id
                AND LOWER (p.promotion_type) = LOWER ('goalquest')
                AND LOWER (p.award_type) = LOWER ('merchandise')
                AND un.user_id = a.user_id
                AND un.is_primary = 1
                AND GP.PROMOTION_ID = a.promotion_id
                AND gp.user_id = a.user_id
                AND gp.goallevel_id = ggl.goallevel_id
                AND (trunc(mo.date_created) >= trunc(p_in_start_date) AND trunc(mo.date_created) <= trunc(p_in_end_date) --06/03/2015 Bug 62484  --06/10/2015
                      OR trunc(mo.date_modified) > trunc(p_in_start_date) AND trunc(mo.date_modified) <= trunc(p_in_end_date));  --06/10/2015

      CURSOR cur_total_issued
      IS
         SELECT   promotion_id, country_id, program_id, om_level_name,node_id,award_date,
                  COUNT (status) tot_count
             FROM tmp_award_item_activity_rpt
            WHERE mo_date_modified >= p_in_start_date       --02/18/2016
         GROUP BY promotion_id, country_id, program_id, om_level_name,node_id,award_date;

      CURSOR cur_total_status (p_status VARCHAR2)
      IS
         SELECT   promotion_id, country_id, program_id, om_level_name,node_id,award_date,
                  COUNT (1) stat_count
             FROM tmp_award_item_activity_rpt
            WHERE status = p_status
        GROUP BY promotion_id, country_id, program_id, om_level_name,node_id,award_date;
   BEGIN
    --INSERT_RPT_REFRESH_DATE('awarditemactivity', 'summary');
    --DELETE      rpt_award_item_activity;

      FOR rec_award_item_activity IN cur_award_item_activity
      LOOP
         --determines status of the order
         IF (rec_award_item_activity.mo_gift_code IS NOT NULL)
         THEN
         IF (LOWER (rec_award_item_activity.merch_gift_code_type) = 'product'
            )
         THEN
            IF (rec_award_item_activity.is_redeemed = 1)
            THEN
               v_status := 'R';
            ELSE
               v_status := 'U';
            END IF;
         ELSE
            --Future code will be here to check if merch_gift_code_type = 'level' is RI or IS status
            IF (rec_award_item_activity.is_redeemed = 1)
            THEN
               v_status := 'R';
            ELSE
               IF (NVL(rec_award_item_activity.expiration_date, TO_DATE('2999/12/31', 'YYYY/MM/DD')) > SYSDATE)
               THEN
                  v_status := 'U';
               ELSE
                  v_status := 'E';
               END IF;

            END IF;
         END IF;
         ELSE
         v_status := NULL;
         END IF;

            INSERT INTO tmp_award_item_activity_rpt
                        (promotion_id,
                         country_id,
                         program_id,
                         om_level_name,
                         gift_code, 
                         status,
                         node_id,
                         award_date,
                         mo_date_modified       --02/09/2016
                        )
                 VALUES (rec_award_item_activity.promotion_id,
                         rec_award_item_activity.country_id,
                         rec_award_item_activity.program_id,
                         rec_award_item_activity.om_level_name,
                         rec_award_item_activity.mo_gift_code, 
                         v_status,
                         rec_award_item_activity.node_id,
                         rec_award_item_activity.award_date,
                         rec_award_item_activity.date_created --02/09/2016 --date_modified --02/18/2016
                        );
      END LOOP;

      FOR rec_total_issued IN cur_total_issued
      LOOP
        
        BEGIN
           
          SELECT 1
            INTO v_exist_data 
            FROM rpt_award_item_activity
           WHERE NVL(program_id,1)    = NVL(rec_total_issued.program_id,1) --02/18/2016
             AND om_level_name = rec_total_issued.om_level_name 
             AND promotion_id  = rec_total_issued.promotion_id
             AND NVL(country_id,1)    = NVL(rec_total_issued.country_id,1) --02/18/2016
             AND NVL(node_id,0)                 = NVL(rec_total_issued.node_id,0)
             AND NVL(award_date,v_default_date) = NVL(rec_total_issued.award_date,v_default_date);  
            
          UPDATE rpt_award_item_activity
             SET tot_num_issued = (NVL(tot_num_issued,0)+rec_total_issued.tot_count),
                 date_modified  = SYSDATE,
                 modified_by    = p_in_user_id
           WHERE NVL(program_id,1)    = NVL(rec_total_issued.program_id,1) --05/29/2015 Bug 62484
             AND om_level_name = rec_total_issued.om_level_name
             AND promotion_id  = rec_total_issued.promotion_id
             AND NVL(country_id,1)    = NVL(rec_total_issued.country_id,1) --02/18/2016
             AND NVL(node_id,0)                    = NVL(rec_total_issued.node_id,0)
             AND NVL(award_date,v_default_date)    = NVL(rec_total_issued.award_date,v_default_date);  
        EXCEPTION 
         WHEN NO_DATA_FOUND THEN
          INSERT INTO rpt_award_item_activity(promotion_id,
                                              country_id,
                                              program_id,                                              
                                              om_level_name,
                                              node_id,
                                              award_date,
                                              tot_num_issued,
                                              tot_num_redeemed, 
                                              tot_num_unredeemed,
                                              tot_num_expired,
                                              date_created,
                                              created_by)
                                      VALUES (rec_total_issued.promotion_id,
                                              rec_total_issued.country_id,
                                              rec_total_issued.program_id,
                                              rec_total_issued.om_level_name,
                                              rec_total_issued.node_id,
                                              rec_total_issued.award_date,
                                              rec_total_issued.tot_count,
                                              NULL, 
                                              NULL,
                                              NULL,
                                              SYSDATE,
                                              p_in_user_id);
        END;
      END LOOP;

      FOR rec_total_issued IN cur_total_status ('E')
      LOOP
        UPDATE rpt_award_item_activity r
           SET tot_num_expired = NVL(tot_num_expired,0)+rec_total_issued.stat_count
         WHERE promotion_id  = rec_total_issued.promotion_id
           AND NVL(country_id,1)    = NVL(rec_total_issued.country_id,1) --05/29/2015 Bug 62484
           AND NVL(program_id,1)    = NVL(rec_total_issued.program_id,1) --05/29/2015 Bug 62484
           AND om_level_name = rec_total_issued.om_level_name
           AND NVL(node_id,0)                  = NVL(rec_total_issued.node_id,0)
           AND NVL(award_date,v_default_date)  = NVL(rec_total_issued.award_date,v_default_date);
           
        UPDATE rpt_award_item_activity                                   --02/09/2016 Bug 65619
            SET tot_num_unredeemed = tot_num_unredeemed - rec_total_issued.stat_count 
          WHERE promotion_id  = rec_total_issued.promotion_id
            AND NVL(country_id,1)    = NVL(rec_total_issued.country_id,1) 
            AND NVL(program_id,1)    = NVL(rec_total_issued.program_id,1) 
            AND om_level_name = rec_total_issued.om_level_name
            AND NVL(node_id,0)                 = NVL(rec_total_issued.node_id,0)
            AND NVL(award_date,v_default_date) = NVL(rec_total_issued.award_date,v_default_date);
      END LOOP;


      FOR rec_total_issued IN cur_total_status ('R')
      LOOP
         UPDATE rpt_award_item_activity
            SET tot_num_redeemed = NVL(tot_num_redeemed,0)+rec_total_issued.stat_count
          WHERE promotion_id = rec_total_issued.promotion_id
            AND NVL(country_id,1)    = NVL(rec_total_issued.country_id,1) --05/29/2015 Bug 62484
            AND NVL(program_id,1)    = NVL(rec_total_issued.program_id,1) --05/29/2015 Bug 62484
            AND om_level_name = rec_total_issued.om_level_name
            AND NVL(node_id,0)                 = NVL(rec_total_issued.node_id,0)
            AND NVL(award_date,v_default_date) = NVL(rec_total_issued.award_date,v_default_date);
            
          UPDATE rpt_award_item_activity                                   --02/09/2016 Bug 65619
            SET tot_num_unredeemed = tot_num_unredeemed - rec_total_issued.stat_count
          WHERE promotion_id  = rec_total_issued.promotion_id
            AND NVL(country_id,1)    = NVL(rec_total_issued.country_id,1) 
            AND NVL(program_id,1)    = NVL(rec_total_issued.program_id,1) 
            AND om_level_name = rec_total_issued.om_level_name
            AND NVL(node_id,0)                 = NVL(rec_total_issued.node_id,0)
            AND NVL(award_date,v_default_date) = NVL(rec_total_issued.award_date,v_default_date);
      END LOOP;
      
      FOR rec_total_issued IN cur_total_status ('U')  
      LOOP
         UPDATE rpt_award_item_activity
            SET tot_num_unredeemed = NVL(tot_num_unredeemed,0)+rec_total_issued.stat_count
          WHERE promotion_id  = rec_total_issued.promotion_id
            AND NVL(country_id,1)    = NVL(rec_total_issued.country_id,1) --05/29/2015 Bug 62484
            AND NVL(program_id,1)    = NVL(rec_total_issued.program_id,1) --05/29/2015 Bug 62484
            AND om_level_name = rec_total_issued.om_level_name
            AND NVL(node_id,0)                 = NVL(rec_total_issued.node_id,0)
            AND NVL(award_date,v_default_date) = NVL(rec_total_issued.award_date,v_default_date);
      END LOOP;
      
      p_out_return_code:= 00;
   EXCEPTION
      WHEN OTHERS THEN
      p_out_return_code := 99;
         prc_execution_log_entry ('P_AWARD_ITEM_ACTIVITY_SA',
                                   1,
                                  'ERROR',
                                  SQLERRM,
                                  NULL);
       p_out_error_message:= SQLERRM;
   END P_AWARD_ITEM_ACTIVITY_SA;

PROCEDURE P_AWARD_ITEM_SELECTION_SA( p_in_user_id           IN  NUMBER,
                                     p_in_start_date        IN  DATE,
                                     p_in_end_date          IN  DATE,
                                     p_out_return_code      OUT NUMBER,
                                     p_out_error_message    OUT VARCHAR2) IS

  CURSOR cur_award_item_selection IS
   SELECT b.promotion_id, 
          b.promotion_name, 
          b.country_id, 
          b.country_name, 
          b.program_id, 
          om_level_name,                                                        
          a.reference_number, 
          fnc_java_decrypt (a.gift_code) mo_gift_code, 
          a.order_number,
          a.node_id,
          a.award_date
    FROM (SELECT c.promotion_id, 
                 p.promotion_name, 
                 mo.order_number, 
                 mo.promo_merch_program_level_id, 
                 mo.gift_code,
                 mo.merch_gift_code_type, 
                 mo.expiration_date, 
                 mo.is_redeemed, 
                 mo.reference_number,
                 c.node_id,
                 TRUNC(mo.date_created) award_date
            FROM merch_order mo, 
                 claim c, 
                 promotion p
           WHERE mo.claim_id = c.claim_id(+)
             AND c.promotion_id = p.promotion_id
             AND NOT EXISTS (SELECT r.promotion_id                         --04/01/2019
                                        FROM promo_recognition r,
                                             promotion pp   
                                        WHERE r.promotion_id = pp.promotion_id AND
                                              r.promotion_id = p.promotion_id  AND
                                              --pp.award_type = 'merchandise' AND 
                                              (r.is_include_purl = 1 OR r.include_celebrations = 1))            --Exclude PURL since SA replaces PURL
              ) a,
          (SELECT pmc.promotion_id, 
                  p.promotion_name, 
                  pmc.country_id, 
                  vw_cn.cms_value country_name,                                 
                  pmc.program_id, 
                  pmcl.promo_merch_program_level_id, 
                  vw.cms_value om_level_name                                    
             FROM promo_merch_country pmc,
                  promo_merch_program_level pmcl,
                  promotion p,
                  country con,
                  vw_cms_asset_value vw,                                        
                  vw_cms_asset_value vw_cn                                      
            WHERE p.PROMOTION_ID = pmc.promotion_id
              AND NOT EXISTS (SELECT r.promotion_id                         --04/01/2019
                                            FROM promo_recognition r,
                                                 promotion pp   
                                            WHERE r.promotion_id = pp.promotion_id AND
                                                  r.promotion_id = p.promotion_id  AND
                                                  --pp.award_type = 'merchandise' AND 
                                                  (r.is_include_purl = 1 OR r.include_celebrations = 1))            --Exclude PURL since SA replaces PURL
              AND pmc.promo_merch_country_id = pmcl.promo_merch_country_id
              AND con.country_id = pmc.country_id
              AND vw.asset_code = pmcl.cm_asset_key                             
              AND vw.locale = 'en_US'                                           
              AND vw_cn.asset_code = con.cm_asset_code                          
              AND vw_cn.locale = 'en_US'                                        
              AND LOWER(p.PROMOTION_TYPE) = LOWER('recognition')
              AND LOWER(p.AWARD_TYPE) = LOWER('merchandise')) b
    WHERE a.promo_merch_program_level_id = b.promo_merch_program_level_id
      AND a.promotion_id = b.promotion_id
   UNION 
   SELECT a.promotion_id,p.promotion_name,NULL country_id,NULL country_name,
    NULL program_id,
        'Level '||GGL.sequence_num om_level_name,mo.reference_number,
        fnc_java_decrypt(mo.gift_code) mo_gift_code,mo.order_number, 
        un.node_id,TRUNC(mo.date_created) award_date
    FROM merch_order mo,ACTIVITY_MERCH_ORDER amo,
        activity a,promotion p,user_node un,goalquest_paxgoal gp,goalquest_goallevel ggl
    WHERE mo.merch_order_id = amo.merch_order_id
        AND amo.activity_id = a.activity_id
        AND p.promotion_id = a.promotion_id
        AND LOWER (p.promotion_type) = LOWER ('goalquest')
        AND LOWER (p.award_type) = LOWER ('merchandise')
        AND un.user_id = a.user_id
        AND un.is_primary = 1
        AND GP.PROMOTION_ID = a.promotion_id
        AND gp.user_id = a.user_id
        AND gp.goallevel_id = ggl.goallevel_id;
      
    CURSOR cur_product_selection(p_order_no IN VARCHAR2) IS
     SELECT PRODUCT_ID, DESCR,INV_ITEM_ID
       FROM PS_BPOM_MS_REDMPTN@MSMART.WORLD
      WHERE order_no = p_order_no
        AND bpom_redmptn_type = 'O';
        
    --TEMP variable until DB Link is made
    v_order_no          VARCHAR2(30);
    v_bpom_crt_status   VARCHAR2(6);
    
    v_product_id        NUMBER;
    v_item_name         VARCHAR2(30);
    
    v_count             NUMBER;
    
    v_promotion_id      tmp_award_item_selection_rpt.PROMOTION_ID%TYPE;
    v_country_id        tmp_award_item_selection_rpt.COUNTRY_ID%TYPE;
    v_program_id        tmp_award_item_selection_rpt.PROGRAM_ID%TYPE;
    v_om_level_name     tmp_award_item_selection_rpt.OM_LEVEL_NAME%TYPE;
    v_item              tmp_award_item_selection_rpt.ITEM_NAME%TYPE;
    v_reference_number  merch_order.reference_number%TYPE;
    
    BEGIN
        FOR rec_award_item_selection IN cur_award_item_selection LOOP
            v_reference_number := rec_award_item_selection.reference_number;
            BEGIN
               SELECT ORDER_NO, BPOM_CRT_STATUS
                 INTO v_order_no, v_bpom_crt_status
               FROM  PS_BPOM_CRT_ORD_VW@MSMART.WORLD
               WHERE BPOM_CRT_XREF_NBR = rec_award_item_selection.reference_number
                 AND BPOM_ORD_STATUS = 'W'; 
            EXCEPTION
               WHEN OTHERS THEN
                  v_order_no := NULL;
                  v_bpom_crt_status := NULL;
            END;
            
            IF v_order_no IS NOT NULL AND v_bpom_crt_status IN ('IS','RI') THEN
                FOR rec_product_selection IN cur_product_selection(v_order_no) LOOP
                    INSERT INTO TMP_AWARD_ITEM_SELECTION_RPT(promotion_id, promotion_name, 
                                                             country_id, country_name, 
                                                             program_id, om_level_name, 
                                                             order_number, product_id, 
                                                             item_name,award_date,
                                                             node_id,inv_item_id)
                    VALUES (rec_award_item_selection.promotion_id, rec_award_item_selection.promotion_name, 
                            rec_award_item_selection.country_id, rec_award_item_selection.country_name, 
                            rec_award_item_selection.program_id, rec_award_item_selection.om_level_name, 
                            rec_award_item_selection.order_number, rec_product_selection.product_id, 
                            rec_product_selection.DESCR,rec_award_item_selection.award_date,
                            rec_award_item_selection.node_id, rec_product_selection.inv_item_id);
                END LOOP;
            END IF;
        END LOOP;
        
        
      MERGE INTO rpt_award_item_selection rpt
        USING (        
        SELECT tmp.promotion_id, tmp.promotion_name, 
                      tmp.country_id, tmp.country_name, 
                      tmp.program_id, tmp.om_level_name, 
                      tmp.item_name,  tmp.award_date,
                       tmp.inv_item_id,
                      tmp.node_id, count(1) item_count
             FROM tmp_award_item_selection_rpt tmp
           GROUP BY tmp.promotion_id, 
                    tmp.promotion_name, 
                    tmp.country_id, 
                    tmp.country_name, 
                    tmp.program_id, 
                    tmp.om_level_name, 
                    tmp.item_name,
                    tmp.award_date,
                    tmp.inv_item_id,                     
                    tmp.node_id                                        
                    ) e
        ON ( rpt.promotion_id       = e.promotion_id  
         AND NVL(rpt.country_id,1)  = NVL(e.country_id,1) 
         AND NVL(rpt.program_id,1)  = NVL(e.program_id,1) 
         AND rpt.om_level_name      = e.om_level_name
         AND rpt.item_name          = e.item_name
         AND rpt.inv_item_id        = e.inv_item_id  
         AND rpt.award_date         = e.award_date
         AND rpt.node_id            = e.node_id)
        WHEN MATCHED THEN
          UPDATE 
             SET 
                 rpt.item_count         = e.item_count, 
                 rpt.country_name       = e.country_name,
                 rpt.promotion_name     = e.promotion_name,
                 rpt.date_modified      = SYSDATE,
                 rpt.modified_by        = P_IN_USER_ID
           WHERE rpt.promotion_id       = e.promotion_id  
             AND NVL(rpt.country_id,1)  = NVL(e.country_id,1) 
             AND NVL(rpt.program_id,1)  = NVL(e.program_id,1) 
             AND rpt.om_level_name      = e.om_level_name
             AND rpt.item_name          = e.item_name
             AND rpt.inv_item_id        = e.inv_item_id  
             AND rpt.award_date         = e.award_date
             AND rpt.node_id            = e.node_id
        WHEN NOT MATCHED THEN
           INSERT (promotion_id,
                  promotion_name,
                  country_id,
                  country_name,
                  program_id,
                  om_level_name,
                  item_name,
                  inv_item_id,                  
                  item_count,
                  award_date,
                  node_id,
                  created_by,
                  date_created)
           VALUES(e.promotion_id, 
                  e.promotion_name, 
                  e.country_id, 
                  e.country_name, 
                  e.program_id, 
                  e.om_level_name, 
                  e.item_name,
                  e.inv_item_id,                      
                  e.item_count,
                  e.award_date,
                  e.node_id,
                  P_IN_USER_ID,
                  SYSDATE);         
        
                   
    EXCEPTION
      WHEN OTHERS THEN
         prc_execution_log_entry ('P_AWARD_ITEM_SELECTION_SA',
                                   1,
                                  'ERROR',
                                  'Failed on reference number '||v_reference_number||' '||SQLERRM,
                                  NULL);
                       
    END P_AWARD_ITEM_SELECTION_SA;
   
END;

/
