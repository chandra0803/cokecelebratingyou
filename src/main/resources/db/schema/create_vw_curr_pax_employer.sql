CREATE OR REPLACE VIEW vw_curr_pax_employer AS
SELECT -- get most recent participant employer record per user
       r.*
  FROM ( -- rank records by termination date and employer index in reverse order
         select RANK() OVER (PARTITION BY pe.user_id ORDER BY pe.termination_date DESC, pe.participant_employer_index DESC) as rec_rank,
                pe.*
           FROM participant_employer pe
       ) r
    -- the current employment record has the lowest ranking
 WHERE r.rec_rank = 1
/