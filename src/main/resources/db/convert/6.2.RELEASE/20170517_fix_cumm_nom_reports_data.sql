MERGE INTO rpt_nomination_detail d
   USING (  -- build report data
            SELECT -- nomination fields
                   c.claim_id,
                   ci.claim_item_id,
                   c.promotion_id,
                   p.promotion_name,
                   p.promotion_status,
                   TRUNC(c.submission_date) AS date_submitted,
                   TRUNC(ci.date_approved) AS date_approved,
                   c.is_open,
                   nc.step_number,
                   c.approval_round AS claim_approval_round,
                   nc.status AS claim_status,
                   ci.approval_status_type AS claim_item_status,
                   nc.nomination_time_period_id, 
                   pntp.time_period_name,
                   c.claim_group_id,
                   nc.team_id,
                   nc.team_name,
                   -- nominator fields
                   c.submitter_id AS giver_user_id,
                   c.node_id AS giver_node_id,
                   ua_g.country_id AS giver_country_id,
                   paxe_g.department_type AS giver_department_type,
                   paxe_g.position_type AS giver_position_type,
                   DECODE(au_g.is_active, 1, 'active', 'inactive') AS giver_pax_status,
                   au_g.user_name AS giver_user_name,
                   INITCAP(au_g.first_name) AS giver_first_name,
                   INITCAP(TRIM(au_g.middle_name)) AS giver_middle_name,
                   INITCAP(au_g.last_name) AS giver_last_name,
                   n_g.name AS giver_node_name,
                   un_g.role AS giver_org_role,
                   -- receiver fields
                   cr.participant_id AS recvr_user_id,
                   cr.node_id AS recvr_node_id,
                   ua_r.country_id AS recvr_country_id,
                   paxe_r.department_type AS recvr_department_type,
                   paxe_r.position_type AS recvr_position_type,
                   DECODE(au_r.is_active, 1, 'active', 'inactive') AS recvr_pax_status,
                   au_r.user_name AS recvr_user_name,
                   INITCAP(au_r.first_name) AS recvr_first_name,
                   INITCAP(TRIM(au_r.middle_name)) AS recvr_middle_name,
                   INITCAP(au_r.last_name) AS recvr_last_name,
                   n_r.name AS recvr_node_name,
                   un_r.role AS recvr_org_role,
                   -- award fields
                   0 AS cash_award_amt,
                   0 AS points_award_amt,
                   0 AS other_award_amt,
                   par.other_award_desc, 
                   -- pivot approval records into approval round fields
                   -- round 1
                   par.r1_award_type,
                   MAX(DECODE(cia.approval_round, 1, cia.approval_status_type)) AS r1_approval_status,
                   MAX(DECODE(cia.approval_round, 1, TRUNC(cia.date_approved))) AS r1_approval_date, 
                   MAX(DECODE(cia.approval_round, 1, cia.approver_user_id))     AS r1_approver_user_id, 
                   MAX(DECODE(cia.approval_round, 1, au_a.user_name))           AS r1_approver_user_name, 
                   MAX(DECODE(cia.approval_round, 1, INITCAP(au_a.first_name))) AS r1_approver_first_name, 
                   MAX(DECODE(cia.approval_round, 1, INITCAP(au_a.last_name)))  AS r1_approver_last_name, 
                   -- round 2
                   par.r2_award_type,
                   MAX(DECODE(cia.approval_round, 2, cia.approval_status_type)) AS r2_approval_status,
                   MAX(DECODE(cia.approval_round, 2, TRUNC(cia.date_approved))) AS r2_approval_date, 
                   MAX(DECODE(cia.approval_round, 2, cia.approver_user_id))     AS r2_approver_user_id, 
                   MAX(DECODE(cia.approval_round, 2, au_a.user_name))           AS r2_approver_user_name, 
                   MAX(DECODE(cia.approval_round, 2, INITCAP(au_a.first_name))) AS r2_approver_first_name, 
                   MAX(DECODE(cia.approval_round, 2, INITCAP(au_a.last_name)))  AS r2_approver_last_name, 
                   -- round 3
                   par.r3_award_type,
                   MAX(DECODE(cia.approval_round, 3, cia.approval_status_type)) AS r3_approval_status,
                   MAX(DECODE(cia.approval_round, 3, TRUNC(cia.date_approved))) AS r3_approval_date, 
                   MAX(DECODE(cia.approval_round, 3, cia.approver_user_id))     AS r3_approver_user_id, 
                   MAX(DECODE(cia.approval_round, 3, au_a.user_name))           AS r3_approver_user_name, 
                   MAX(DECODE(cia.approval_round, 3, INITCAP(au_a.first_name))) AS r3_approver_first_name, 
                   MAX(DECODE(cia.approval_round, 3, INITCAP(au_a.last_name)))  AS r3_approver_last_name, 
                   -- round 4
                   par.r4_award_type,
                   MAX(DECODE(cia.approval_round, 4, cia.approval_status_type)) AS r4_approval_status,
                   MAX(DECODE(cia.approval_round, 4, TRUNC(cia.date_approved))) AS r4_approval_date, 
                   MAX(DECODE(cia.approval_round, 4, cia.approver_user_id))     AS r4_approver_user_id, 
                   MAX(DECODE(cia.approval_round, 4, au_a.user_name))           AS r4_approver_user_name, 
                   MAX(DECODE(cia.approval_round, 4, INITCAP(au_a.first_name))) AS r4_approver_first_name, 
                   MAX(DECODE(cia.approval_round, 4, INITCAP(au_a.last_name)))  AS r4_approver_last_name, 
                   -- round 5
                   par.r5_award_type,
                   MAX(DECODE(cia.approval_round, 5, cia.approval_status_type)) AS r5_approval_status,
                   MAX(DECODE(cia.approval_round, 5, TRUNC(cia.date_approved))) AS r5_approval_date, 
                   MAX(DECODE(cia.approval_round, 5, cia.approver_user_id))     AS r5_approver_user_id, 
                   MAX(DECODE(cia.approval_round, 5, au_a.user_name))           AS r5_approver_user_name, 
                   MAX(DECODE(cia.approval_round, 5, INITCAP(au_a.first_name))) AS r5_approver_first_name, 
                   MAX(DECODE(cia.approval_round, 5, INITCAP(au_a.last_name)))  AS r5_approver_last_name
              FROM claim c,
                   nomination_claim nc,
                   claim_item ci,
                   claim_recipient cr,
                   promotion p,
                   promo_nomination_time_period pntp,
                   ( -- flatten promotion award rounds
                     SELECT pnl.promotion_id,
                            MAX(DECODE(pnl.level_index, 1, pnl.award_payout_type)) AS r1_award_type,
                            MAX(DECODE(pnl.level_index, 2, pnl.award_payout_type)) AS r2_award_type,
                            MAX(DECODE(pnl.level_index, 3, pnl.award_payout_type)) AS r3_award_type,
                            MAX(DECODE(pnl.level_index, 4, pnl.award_payout_type)) AS r4_award_type,
                            MAX(DECODE(pnl.level_index, 5, pnl.award_payout_type)) AS r5_award_type,
                            LISTAGG(DECODE(pnl.award_payout_type, 'other', pnl.payout_description), ', ')
                              WITHIN GROUP (ORDER BY pnl.level_index) AS other_award_desc
                       FROM promo_nomination_level pnl
                      GROUP BY pnl.promotion_id
                   ) par,
                   -- approval
                   claim_item_approver cia,
                   application_user au_a,
                   -- giver
                   application_user au_g,
                   user_address ua_g,
                   rpt_participant_employer paxe_g,
                   node n_g,
                   user_node un_g,
                   -- receiver
                   application_user au_r,
                   user_address ua_r,
                   rpt_participant_employer paxe_r,
                   node n_r,
                   user_node un_r
             WHERE --p_in_start_date < c.date_created
--                c.date_created <= p_in_end_date
                c.claim_id = nc.claim_id
               AND nc.status = 'complete'
               AND c.claim_group_id IS NOT NULL
               AND c.claim_id = ci.claim_id
               AND ci.claim_item_id = cr.claim_item_id
               AND c.promotion_id = p.promotion_id
               AND p.promotion_status IN ('live', 'expired')
               AND p.is_deleted = 0
               AND nc.nomination_time_period_id = pntp.nomination_time_period_id (+)
               AND c.promotion_id = par.promotion_id
                -- link approvals
                 AND (ci.claim_item_id = cia.claim_item_id 
                                     OR c.claim_group_id = cia.claim_group_id )
               AND cia.approver_user_id = au_a.user_id (+)
                -- link nominator details
               AND c.submitter_id = au_g.user_id (+)
               AND c.submitter_id = ua_g.user_id (+)
               AND 1 = ua_g.is_primary (+)
               AND c.submitter_id = paxe_g.user_id (+)
               AND c.node_id = n_g.node_id (+)
               AND c.submitter_id = un_g.user_id (+)
               AND c.node_id = un_g.node_id (+)
               AND 1 = un_g.is_primary (+)
               AND 1 = un_g.status (+)
                -- link nominee details
               AND cr.participant_id = au_r.user_id (+)
               AND cr.participant_id = ua_r.user_id (+)
               AND 1 = ua_r.is_primary (+)
               AND cr.participant_id = paxe_r.user_id (+)
               AND cr.node_id = n_r.node_id (+)
               AND cr.participant_id = un_r.user_id (+)
               AND cr.node_id = un_r.node_id (+)
               AND 1 = un_r.is_primary (+)
               AND 1 = un_r.status (+)
             GROUP BY c.claim_id,
                   ci.claim_item_id,
                   c.promotion_id,
                   p.promotion_name,
                   p.promotion_status,
                   TRUNC(c.submission_date),
                   TRUNC(ci.date_approved),
                   c.is_open,
                   nc.step_number,
                   c.approval_round,
                   nc.status,
                   ci.approval_status_type,
                   nc.nomination_time_period_id, 
                   pntp.time_period_name,
                   c.claim_group_id,
                   nc.team_id,
                   nc.team_name,
                   -- nominator fields
                   c.submitter_id,
                   c.node_id,
                   ua_g.country_id,
                   paxe_g.department_type,
                   paxe_g.position_type,
                   au_g.is_active,
                   au_g.user_name,
                   au_g.first_name,
                   au_g.middle_name,
                   au_g.last_name,
                   n_g.name,
                   un_g.role,
                   -- nominee fields
                   cr.participant_id,
                   cr.node_id,
                   ua_r.country_id,
                   paxe_r.department_type,
                   paxe_r.position_type,
                   au_r.is_active,
                   au_r.user_name,
                   au_r.first_name,
                   au_r.middle_name,
                   au_r.last_name,
                   n_r.name,
                   un_r.role,
                   par.other_award_desc, 
                   par.r1_award_type,
                   par.r2_award_type,
                   par.r3_award_type,
                   par.r4_award_type,
                   par.r5_award_type
         ) s
      ON (d.claim_item_id = s.claim_item_id)
      WHEN MATCHED THEN
      UPDATE
      SET R1_APPROVAL_STATUS = S.R1_APPROVAL_STATUS,
          R1_APPROVAL_DATE	= S.R1_APPROVAL_DATE,
          R1_APPROVER_USER_ID	= S.R1_APPROVER_USER_ID,
          R1_APPROVER_USER_NAME	= S.R1_APPROVER_USER_NAME,
          R1_APPROVER_FIRST_NAME = S.R1_APPROVER_FIRST_NAME,
          R1_APPROVER_LAST_NAME	= S.R1_APPROVER_LAST_NAME,
          R2_AWARD_TYPE	= S.R2_AWARD_TYPE,
          R2_APPROVAL_STATUS	= S.R2_APPROVAL_STATUS,
          R2_APPROVAL_DATE	= S.R2_APPROVAL_DATE,
          R2_APPROVER_USER_ID	= S.R2_APPROVER_USER_ID,
          R2_APPROVER_USER_NAME	= S.R2_APPROVER_USER_NAME,
          R2_APPROVER_FIRST_NAME	= S.R2_APPROVER_FIRST_NAME,
          R2_APPROVER_LAST_NAME	= S.R2_APPROVER_LAST_NAME,
          R3_AWARD_TYPE	= S.R3_AWARD_TYPE,
          R3_APPROVAL_STATUS	= S.R3_APPROVAL_STATUS,
          R3_APPROVAL_DATE	= S.R3_APPROVAL_DATE,
          R3_APPROVER_USER_ID	= S.R3_APPROVER_USER_ID,
          R3_APPROVER_USER_NAME	= S.R3_APPROVER_USER_NAME,
          R3_APPROVER_FIRST_NAME	= S.R3_APPROVER_FIRST_NAME,
          R3_APPROVER_LAST_NAME	= S.R3_APPROVER_LAST_NAME,
          R4_AWARD_TYPE	= S.R4_AWARD_TYPE,
          R4_APPROVAL_STATUS	= S.R4_APPROVAL_STATUS,
          R4_APPROVAL_DATE	= S.R4_APPROVAL_DATE,
          R4_APPROVER_USER_ID	= S.R4_APPROVER_USER_ID,
          R4_APPROVER_USER_NAME	= S.R4_APPROVER_USER_NAME,
          R4_APPROVER_FIRST_NAME	= S.R4_APPROVER_FIRST_NAME,
          R4_APPROVER_LAST_NAME	= S.R4_APPROVER_LAST_NAME,
          R5_AWARD_TYPE	= S.R5_AWARD_TYPE,
          R5_APPROVAL_STATUS	= S.R5_APPROVAL_STATUS,
          R5_APPROVAL_DATE	= S.R5_APPROVAL_DATE,
          R5_APPROVER_USER_ID	= S.R5_APPROVER_USER_ID,
          R5_APPROVER_USER_NAME	= S.R5_APPROVER_USER_NAME,
          R5_APPROVER_FIRST_NAME	= S.R5_APPROVER_FIRST_NAME,
          R5_APPROVER_LAST_NAME	= S.R5_APPROVER_LAST_NAME
/
