CREATE OR REPLACE 
PROCEDURE prc_stack_ranking
   ( p_in_stack_rank_id IN NUMBER,
     p_out_result_code OUT VARCHAR2)
   IS

/****************************************************************************
--
--
-- Purpose: To create stack rank list and assign rank
--
-- MODIFICATION HISTORY
-- Person      Date    Comments
-- ---------   ------  -------------------------------------------
-- Percy M.   03/20/06  Initial creation
-- Gorantla   08/28/19  Bug 77739 - Nomination with the comments of 4000 characters length is not loading on nomination approval page.      
***************************************************************************/

CURSOR cur_main IS
       SELECT *
       FROM STACK_RANK
       WHERE stack_rank_id = p_in_stack_rank_id
         AND LOWER(state) = 'creating_stackrank_lists';



CURSOR cur_group (cp_in_promotion_id promotion.promotion_id%TYPE) IS
       SELECT * FROM STACK_RANK_PAYOUT_GROUP
       WHERE promotion_id = cp_in_promotion_id;



CURSOR cur_stack_rank_node (cp_in_promotion_id IN NUMBER ,
                            cp_in_start_date IN DATE ,
                            cp_in_end_date IN DATE ,
                            cp_in_node_type_id IN NUMBER ) IS
      SELECT node_id
        FROM claim_item a,
						 claim b,
						 claim_product c
       WHERE b.promotion_id = ( SELECT NVL(parent_promotion_id, cp_in_promotion_id)
			 			 									  FROM promo_product_claim
																WHERE promotion_id = cp_in_promotion_id )
			   AND a.claim_id = b.claim_id
			   AND is_open = 0
         AND lower(a.approval_status_type) LIKE '%approv%'
         AND a.claim_item_id = c.claim_item_id
         AND TRUNC(b.submission_date) BETWEEN cp_in_start_date AND cp_in_end_date
         AND b.is_open = 0
         AND b.node_id IN (SELECT node_id
                            FROM node
                            WHERE node_type_id = cp_in_node_type_id)
       GROUP BY node_id
             ;

CURSOR cur_stack_rank_pax (cp_in_promotion_id IN NUMBER ,
                            cp_in_start_date IN DATE ,
                            cp_in_end_date IN DATE ,
                            cp_in_node_id IN NUMBER ) IS
      SELECT submitter_id, SUM(product_qty) qty
        FROM claim_item a,
						 claim b,
						 claim_product c
       WHERE b.promotion_id = ( SELECT NVL(parent_promotion_id, cp_in_promotion_id)
			 			 									  FROM promo_product_claim
																WHERE promotion_id = cp_in_promotion_id )
			   AND a.claim_id = b.claim_id
			   AND is_open = 0
         AND lower(a.approval_status_type) LIKE '%approv%'
         AND a.claim_item_id = c.claim_item_id
         AND TRUNC(b.submission_date) BETWEEN cp_in_start_date AND cp_in_end_date
         AND b.is_open = 0
         AND b.node_id = cp_in_node_id
       GROUP BY submitter_id
               ;


CURSOR cur_stack_rank_all_node (cp_in_promotion_id IN NUMBER ,
                            cp_in_start_date IN DATE ,
                            cp_in_end_date IN DATE ,
                            cp_in_node_type_id IN NUMBER ) IS
SELECT n.node_id
FROM node n
WHERE node_type_id = cp_in_node_type_id
  AND 1 =   (SELECT DECODE( COUNT(*), 0, 0, 1 )
        FROM claim_item a,
						 claim b,
						 claim_product c
       WHERE b.promotion_id = ( SELECT NVL(parent_promotion_id, cp_in_promotion_id)
			 			 									  FROM promo_product_claim
																WHERE promotion_id = cp_in_promotion_id )
			   AND a.claim_id = b.claim_id
			   AND is_open = 0
         AND lower(a.approval_status_type) LIKE '%approv%'
         AND a.claim_item_id = c.claim_item_id
         AND TRUNC(b.submission_date) BETWEEN cp_in_start_date AND cp_in_end_date
         AND b.node_id IN (SELECT node_id
                       		 FROM node
                           CONNECT BY PRIOR node_id = parent_node_id
                           START WITH node_id = n.node_id)
                           )     ;
                           
                           
CURSOR cur_stack_rank_all_pax (cp_in_promotion_id IN NUMBER ,
                            cp_in_start_date IN DATE ,
                            cp_in_end_date IN DATE ,
                            cp_in_node_id IN NUMBER ) IS
      SELECT submitter_id, SUM(product_qty) qty
        FROM claim_item a,
						 claim b,
						 claim_product c
       WHERE b.promotion_id = ( SELECT NVL(parent_promotion_id, cp_in_promotion_id)
			 			 									  FROM promo_product_claim
																WHERE promotion_id = cp_in_promotion_id )
			   AND a.claim_id = b.claim_id
         AND lower(a.approval_status_type) LIKE '%approv%'
         AND a.claim_item_id = c.claim_item_id
         AND TRUNC(b.submission_date) BETWEEN cp_in_start_date AND cp_in_end_date
         AND b.is_open = 0
         AND b.node_id IN (SELECT node_id
                       		 FROM node
                           CONNECT BY PRIOR node_id = parent_node_id
                           START WITH node_id = cp_in_node_id)
       GROUP BY submitter_id
       ;
       
       

CURSOR cur_stack_rank_pax_cfse (cp_in_promotion_id IN NUMBER ,
                            cp_in_start_date IN DATE ,
                            cp_in_end_date IN DATE ,
                            cp_in_node_id IN NUMBER ) IS
   SELECT submitter_id, SUM(NVL(to_char(b.VALUE),0)) cfse_value  -- 08/28/2019
     FROM promo_product_claim a,
          claim_cfse b,
          claim c,
          claim_item d
    WHERE c.promotion_id = ( SELECT NVL(parent_promotion_id, cp_in_promotion_id)
			 			 									  FROM promo_product_claim
																WHERE promotion_id = cp_in_promotion_id )
      AND a.stack_rank_cfse_id = b.claim_form_step_element_id
      AND c.claim_id           = b.claim_id
      AND is_open = 0
      AND TRUNC(c.submission_date) BETWEEN cp_in_start_date AND cp_in_end_date
      AND c.node_id  = cp_in_node_id
      AND b.claim_id = d.claim_id
      AND LOWER(approval_status_type) LIKE '%approv%'
    GROUP BY submitter_id
          ;



CURSOR cur_stack_rank_all_pax_cfse (cp_in_promotion_id IN NUMBER ,
                            cp_in_start_date IN DATE ,
                            cp_in_end_date IN DATE ,
                            cp_in_node_id IN NUMBER ) IS
   SELECT submitter_id, SUM(NVL(to_char(b.VALUE),0)) cfse_value  -- 08/28/2019
     FROM promo_product_claim a,
          claim_cfse b,
          claim c,
          claim_item d
    WHERE c.promotion_id = ( SELECT NVL(parent_promotion_id, cp_in_promotion_id)
			 			 									  FROM promo_product_claim
																WHERE promotion_id = cp_in_promotion_id )
      AND a.stack_rank_cfse_id = b.claim_form_step_element_id
      AND c.claim_id           = b.claim_id
      AND TRUNC(c.submission_date) BETWEEN cp_in_start_date AND cp_in_end_date
  	  AND is_open = 0
      AND c.node_id  IN  (SELECT node_id
                       		 FROM node
                           CONNECT BY PRIOR node_id = parent_node_id
                           START WITH node_id = cp_in_node_id)
      AND b.claim_id = d.claim_id
      AND LOWER(approval_status_type) LIKE '%approv%'
    GROUP BY submitter_id
    ;
    

CURSOR cur_group_nodes IS
       SELECT stack_rank_payout_group_id, calculate_payout, stack_rank_node_id
         FROM stack_rank a,
              stack_rank_payout_group b,
              node c,
              stack_rank_node d
        WHERE a.stack_rank_id = p_in_stack_rank_id
          AND a.promotion_id  = b.promotion_id
          AND b.node_type_id  = c.node_type_id
          AND a.stack_rank_id = d.stack_rank_id
          AND c.node_id       = d.node_id
GROUP BY stack_rank_payout_group_id, calculate_payout, stack_rank_node_id;


CURSOR cur_rank_and_payout (cp_in_stack_rank_node_id IN NUMBER) IS
       SELECT ROWID, a.*
         FROM stack_rank_participant a
        WHERE stack_rank_node_id = cp_in_stack_rank_node_id
        ORDER BY a.stack_rank_factor DESC
        ;


CURSOR cur_tied_payout (cp_in_stack_rank_node_id IN NUMBER) IS
       SELECT RANK ranks_tied, COUNT(*) times_tied
       FROM stack_rank_participant a
       WHERE stack_rank_node_id = cp_in_stack_rank_node_id
       GROUP BY rank HAVING COUNT(*) > 1;
       

v_stack_rank_factor_type        promo_product_claim.stack_rank_factor_type%TYPE;
v_stack_rank_node_id            stack_rank_node.stack_rank_node_id%TYPE;
v_stack_rank_approval_type      promo_product_claim.stack_rank_approval_type%TYPE;

v_rank                          NUMBER(18):=0;
--v_max_rank                      NUMBER(18):=0;
v_ranks_tied                    NUMBER(18):=0;
v_payout                        NUMBER(18):=NULL;
v_total_payout                  NUMBER(18):=0;
v_tied                          NUMBER(18):=0;
v_prev_qty                      NUMBER(18):=0;
v_count                         NUMBER(18):=0;
v_total_count                   NUMBER(18):=0;
v_calculate_payout              stack_rank.calculate_payout%TYPE;
v_stage                         VARCHAR2(10):='0';

stack_rank_id_not_found         EXCEPTION;



BEGIN

  prc_execution_log_entry('PRC_STACK_RANKING',1,'INFO', 'Process Started', null);

    UPDATE STACK_RANK SET state = 'creating_stackrank_lists', date_modified = SYSDATE ,
           modified_by = 0
       WHERE stack_rank_id = p_in_stack_rank_id
         AND LOWER(state) = 'before_creating_stackrank_lists';

    IF SQL%ROWCOUNT < 1 THEN
      RAISE stack_rank_id_not_found;
    END IF;


  FOR v_main IN cur_main LOOP

    v_count := v_count + 1;
    BEGIN
     FOR v_group IN cur_group (v_main.promotion_id) LOOP
     
       SELECT a.stack_rank_factor_type, a.stack_rank_approval_type
         INTO v_stack_rank_factor_type, v_stack_rank_approval_type
       FROM promo_product_claim a
       WHERE promotion_id = v_main.promotion_id;

       IF lower(v_group.submitter_to_rank_type) = 'pax_on_selected_node' THEN

           FOR v_stack_rank_node IN cur_stack_rank_node (v_main.promotion_id,
                                                        v_main.start_date,
                                                        v_main.end_date,
                                                        v_group.node_type_id) LOOP
                                                        
             v_total_count := v_total_count + 1;
             
             INSERT INTO stack_rank_node (
                    stack_rank_node_id,
                   stack_rank_id,
                   node_id,
                   version,
                   created_by,
                   date_created )
                VALUES
                   (STACK_RANK_NODE_PK_SQ.NEXTVAL,
                   v_main.stack_rank_id,
                   v_stack_rank_node.node_id,
                   1,
                   0,
                   SYSDATE)
               ;

         IF LOWER(v_stack_rank_factor_type) = 'quantitysold' THEN
             FOR v_stack_rank_pax IN cur_stack_rank_pax (v_main.promotion_id,
                                                        v_main.start_date,
                                                        v_main.end_date,
                                                        v_stack_rank_node.node_id) LOOP
                   INSERT INTO stack_rank_participant (stack_rank_participant_id,
                          stack_rank_node_id,
                          user_id,
                          stack_rank_factor,
                          RANK,
                          tied,
                          payout,
                          version,
                          created_by,
                          date_created)
                   VALUES (STACK_RANK_PARTICIPANT_PK_SQ.nextval,
                          STACK_RANK_NODE_PK_Sq.CURRVAL ,
                          v_stack_rank_pax.submitter_id,
                          v_stack_rank_pax.qty,
                          0,
                          0,
                          0,
                          1,
                          0,
                          SYSDATE);
             END LOOP;

         ELSIF LOWER(v_stack_rank_factor_type) = 'numericclaimelement' THEN

             FOR v_stack_rank_pax IN cur_stack_rank_pax_cfse (v_main.promotion_id,
                                                        v_main.start_date,
                                                        v_main.end_date,
                                                        v_stack_rank_node.node_id) LOOP
                   INSERT INTO stack_rank_participant (stack_rank_participant_id,
                          stack_rank_node_id,
                          user_id,
                          stack_rank_factor,
                          RANK,
                          tied,
                          payout,
                          version,
                          created_by,
                          date_created)
                   VALUES (STACK_RANK_PARTICIPANT_PK_SQ.nextval,
                          STACK_RANK_NODE_PK_Sq.CURRVAL ,
                          v_stack_rank_pax.submitter_id,
                          v_stack_rank_pax.cfse_value,
                          0,
                          0,
                          0,
                          1,
                          0,
                          SYSDATE);
             END LOOP;
           END IF;
           END LOOP;


       ELSIF lower(v_group.submitter_to_rank_type) = 'pax_on_all_nodes' THEN
       
           FOR v_stack_rank_all_node IN cur_stack_rank_all_node (v_main.promotion_id,
                                                        v_main.start_date,
                                                        v_main.end_date,
                                                        v_group.node_type_id) LOOP
             v_total_count := v_total_count + 1;
             INSERT INTO stack_rank_node (
                    stack_rank_node_id,
                   stack_rank_id,
                   node_id,
                   version,
                   created_by,
                   date_created )
                VALUES
                   (STACK_RANK_NODE_PK_SQ.NEXTVAL,
                   v_main.stack_rank_id,
                   v_stack_rank_all_node.node_id,
                   1,
                   0,
                   SYSDATE)
               ;

             IF LOWER(v_stack_rank_factor_type) = 'quantitysold' THEN
               FOR v_stack_rank_pax IN cur_stack_rank_all_pax (v_main.promotion_id,
                                                        v_main.start_date,
                                                        v_main.end_date,
                                                        v_stack_rank_all_node.node_id) LOOP
                   INSERT INTO stack_rank_participant (stack_rank_participant_id,
                          stack_rank_node_id,
                          user_id,
                          stack_rank_factor,
                          RANK,
                          tied,
                          payout,
                          version,
                          created_by,
                          date_created)
                   VALUES (STACK_RANK_PARTICIPANT_PK_SQ.nextval,
                          STACK_RANK_NODE_PK_Sq.CURRVAL ,
                          v_stack_rank_pax.submitter_id,
                          v_stack_rank_pax.qty,
                          0,
                          0,
                          0,
                          1,
                          0,
                          SYSDATE);
               END LOOP;

             ELSIF LOWER(v_stack_rank_factor_type) = 'numericclaimelement' THEN

               FOR v_stack_rank_pax IN cur_stack_rank_all_pax_cfse (v_main.promotion_id,
                                                        v_main.start_date,
                                                        v_main.end_date,
                                                        v_stack_rank_all_node.node_id) LOOP
                   INSERT INTO stack_rank_participant (stack_rank_participant_id,
                          stack_rank_node_id,
                          user_id,
                          stack_rank_factor,
                          RANK,
                          tied,
                          payout,
                          version,
                          created_by,
                          date_created)
                   VALUES (STACK_RANK_PARTICIPANT_PK_SQ.nextval,
                          STACK_RANK_NODE_PK_Sq.CURRVAL ,
                          v_stack_rank_pax.submitter_id,
                          v_stack_rank_pax.cfse_value,
                          0,
                          0,
                          0,
                          1,
                          0,
                          SYSDATE);
               END LOOP;
             END IF;
           END LOOP;

        END IF;
      END LOOP;

     EXCEPTION
     WHEN OTHERS THEN
        p_out_result_code := '99|0|0';
        prc_execution_log_entry('PRC_STACK_RANKING',1,'ERROR',SQLERRM,null);
     END;
   END LOOP;
   

   FOR v_group_nodes IN cur_group_nodes LOOP
       v_rank     := 0;
       v_tied     := 1;
       v_prev_qty := NULL;
       v_payout   := NULL;
       FOR v_rank_and_payout IN cur_rank_and_payout (v_group_nodes.stack_rank_node_id) LOOP
         IF v_prev_qty = v_rank_and_payout.stack_rank_factor THEN -- if quantities are equal then tie
           v_tied := v_tied + 1;
         ELSE
          IF v_tied > 1 THEN -- if previous rank was tied then skip rank#
            v_rank := v_rank + v_tied;
            v_tied :=1 ;
           ELSE
            v_rank := v_rank + 1;
           END IF;
         END IF;

         v_prev_qty := v_rank_and_payout.stack_rank_factor;
/*
         SELECT MAX(end_rank)
             into v_max_rank
         FROM stack_rank_payout
         WHERE stack_rank_payout_group_id = v_group_nodes.stack_rank_payout_group_id;

         IF v_rank > v_max_rank THEN
           v_rank := v_max_rank;
         END IF;
*/
         IF v_group_nodes.calculate_payout = 1 THEN
           BEGIN
             SELECT payout
              INTO v_payout
             FROM stack_rank_payout
             WHERE stack_rank_payout_group_id = v_group_nodes.stack_rank_payout_group_id
               AND v_rank BETWEEN start_rank AND end_rank;
              
           EXCEPTION WHEN OTHERS THEN
             v_payout := NULL;
           END;
         END IF;

         UPDATE stack_rank_participant
                SET rank   = v_rank,
                    payout = v_payout--,
--                    tied   = v_tied
           WHERE ROWID = v_rank_and_payout.ROWID;
       END LOOP;
/*
       -- update 'tied' column for all tied ranks
       UPDATE stack_rank_participant SET tied = 1 WHERE RANK IN
              (SELECT rank
               FROM stack_rank_participant a
               WHERE stack_rank_node_id = v_group_nodes.stack_rank_node_id
               GROUP BY rank HAVING COUNT(*) > 1)
        AND stack_rank_node_id = v_group_nodes.stack_rank_node_id;
        
*/
       -- update payout for all tied ranks
        FOR v_tied_payout IN cur_tied_payout (v_group_nodes.stack_rank_node_id) LOOP
          v_total_payout := 0;
          v_payout :=0 ;
          v_ranks_tied := v_tied_payout.ranks_tied;
          FOR i IN 1..v_tied_payout.times_tied LOOP
            BEGIN
              SELECT payout
                INTO v_payout
                FROM stack_rank_payout a
               WHERE v_ranks_tied BETWEEN start_rank AND end_rank
                 AND stack_rank_payout_group_id = v_group_nodes.stack_rank_payout_group_id;

              EXCEPTION WHEN OTHERS THEN
                      v_payout := 0;
            END;
            
            v_total_payout := v_total_payout + v_payout;
            v_ranks_tied := v_ranks_tied + 1;
          END LOOP;

          v_payout := ROUND(v_total_payout / v_tied_payout.times_tied);

          UPDATE stack_rank_participant
             SET payout = v_payout,
                 tied   = 1
          WHERE stack_rank_node_id = v_group_nodes.stack_rank_node_id
            AND rank = v_tied_payout.ranks_tied;

      END LOOP;
   END LOOP;
   
   SELECT calculate_payout
          INTO v_calculate_payout
   FROM stack_rank
   WHERE stack_rank_id = p_in_stack_rank_id;
   
   
   IF v_calculate_payout = 1 THEN
     -- if payout is calculated then always manual approve
     UPDATE STACK_RANK SET state = 'waiting_for_stackrank_lists_to_be_approved', date_modified = SYSDATE ,
           modified_by = 0
       WHERE stack_rank_id = p_in_stack_rank_id
         AND LOWER(state) = 'creating_stackrank_lists';
   ELSE
       IF v_stack_rank_approval_type LIKE '%auto%' THEN
         UPDATE STACK_RANK SET state = 'stackrank_lists_approved', date_modified = SYSDATE ,
           modified_by = 0
         WHERE stack_rank_id = p_in_stack_rank_id
           AND LOWER(state) = 'creating_stackrank_lists';
         
        ELSIF v_stack_rank_approval_type LIKE '%manual%' THEN
          UPDATE STACK_RANK SET state = 'waiting_for_stackrank_lists_to_be_approved', date_modified = SYSDATE ,
           modified_by = 0
          WHERE stack_rank_id = p_in_stack_rank_id
            AND LOWER(state) = 'creating_stackrank_lists';
       END IF;
   END IF;


   prc_execution_log_entry('PRC_STACK_RANKING',1,'INFO', 'Process Completed', null);
   p_out_result_code := '0'||'|'||v_count||'|'||v_total_count;



EXCEPTION
    WHEN stack_rank_id_not_found THEN
      prc_execution_log_entry('PRC_STACK_RANKING',1,'ERROR', 'Stank Rank ID '||p_in_stack_rank_id||' not found.', null);
      p_out_result_code := '99|0|0';
    WHEN OTHERS THEN
      prc_execution_log_entry('PRC_STACK_RANKING',1,'ERROR', sqlerrm, null);
      p_out_result_code := '99|0|0';
END; -- Procedure
/

