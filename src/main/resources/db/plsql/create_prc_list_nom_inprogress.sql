CREATE OR REPLACE PROCEDURE prc_list_nom_inprogress (
   p_in_user_id          IN     NUMBER, --SubmitterId
   p_in_rowNumStart         IN  NUMBER,
   p_in_rowNumEnd           IN  NUMBER,
   p_in_sortedBy            IN  VARCHAR2,
   p_in_sortedOn            IN  VARCHAR2,
   p_out_return_code        OUT NUMBER,
   p_out_data               OUT SYS_REFCURSOR)
IS
   /***********************************************************************************
      Purpose:  To provide data for nominations in progress page.

      Person             Date           Comments
      -----------        ----------     --------------------------------------------
    Ravi Dhanekula   07/26/2016     Initial Version
    Chidamba         09/04/2017     G6-2834-Saved Nominations can be submitted after the promotion end date 
    DeepakrajS       04/19/2019     Git#55 - No Records found in Action required Page for more than one incomplete Nomination claims
    Gorantla         08/08/2019     Git#2206 - Record retried from Draft(Incomplete Submission from Action Item) showing multiple Item based on no.of pax added
   ************************************************************************************/

   is_team                    NUMBER (1):=0;

   c_process_name    CONSTANT execution_log.process_name%TYPE := 'PRC_LIST_NOM_INPROGRESS' ;
   c_release_level   CONSTANT execution_log.release_level%TYPE := '1.0';
   v_promotion_id    promotion.promotion_id%TYPE;
BEGIN
   OPEN p_out_data FOR
   SELECT s.*
    FROM (SELECT -- build row number sort field
           ROW_NUMBER() OVER 
           (ORDER BY
             -- sort on field ascending
             (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'date_created')      THEN UPPER(res.date_created)    END) ASC
           , (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'promotion_name')    THEN UPPER(res.promotion_name)  END) ASC
           , (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'Name')    THEN UPPER(res.Name)  END) ASC
             -- sort on field descending
           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'date_created')     THEN UPPER(res.date_created)    END) DESC
           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'promotion_name')   THEN res.promotion_name         END) DESC
           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'Name')   THEN UPPER(res.Name)  END) DESC
           ) AS rn, 
           res.*
      FROM (
   SELECT DISTINCT c.claim_id,c.promotion_id,c.date_created,p.promotion_name,  -- 08/08/2019
   /*COALESCE (nc.team_name,(select last_name||', '||first_name FROM application_user au, claim_recipient cr,claim_item ci WHERE ci.claim_id = c.claim_id AND ci.claim_item_id = cr.claim_item_id AND
                                                                                                                       cr.participant_id = au.user_id)) name*/  --04/19/2019
   COALESCE (nc.team_name,au.last_name||', '||au.first_name ) name --04/19/2019
   FROM claim c, promotion p,nomination_claim nc,
   application_user au, claim_recipient cr,claim_item ci--04/19/2019
   WHERE c.submitter_id = p_in_user_id
   AND c.promotion_id = p.promotion_id
   AND c.claim_id = nc.claim_id
   AND ci.claim_id = c.claim_id --04/19/2019
   AND ci.claim_item_id = cr.claim_item_id --04/19/2019
   AND cr.participant_id = au.user_id --04/19/2019
   AND p.promotion_status = 'live'  --09/04/2017
   AND NVL(p.promotion_end_date, TRUNC(SYSDATE)) >= TRUNC(SYSDATE)  --09/04/2017
   AND nc.status = 'in-complete') res
   ) s -- sort number result set
    WHERE s.rn > TO_NUMBER(p_in_rowNumStart)
      AND s.rn < TO_NUMBER(p_in_rowNumEnd)
    ORDER BY s.rn;
  
EXCEPTION
   WHEN OTHERS
   THEN
      prc_execution_log_entry (
         'PRC_LIST_NOM_INPROGRESS',
         1,
         'ERROR',p_in_user_id
         || ': '
         || 'p_in_user_id'
         || CHR (10)
         || SQLERRM,
         NULL);

      OPEN p_out_data FOR SELECT NULL FROM DUAL;
END;
/
