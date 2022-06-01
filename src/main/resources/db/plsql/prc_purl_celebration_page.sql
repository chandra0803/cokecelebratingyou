CREATE OR REPLACE PROCEDURE prc_purl_celebration_page (
   p_in_past_present   IN     VARCHAR2,
   p_in_tab_type       IN     VARCHAR2,
   p_in_list_value     IN     VARCHAR2,
   p_in_locale         IN     VARCHAR2,
   p_in_user_id        IN     NUMBER,
   p_in_user_name      IN     VARCHAR2,
   p_in_rownum_start   IN     NUMBER,
   p_in_rownum_end     IN     NUMBER,
   p_in_sort_by        IN     VARCHAR2,
   p_in_sort_on        IN     VARCHAR2,
   p_out_returncode       OUT NUMBER,
   p_out_result_set       OUT SYS_REFCURSOR)
IS
   /*********************************************************************************
   -- Purpose:
   -- This procedure returns the result set which will be used for 'Pr' for Admin.
   -- Person           Date        Comments
   -- ---------       ----------   -----------------------------------------------------
   -- Chidamba        05/18/2016   Initial Version
   -- Sherif Basha    06/21/2016   Bug 66351 Form elements need to be concatenated in procedure.It was done in Java before
   -- Sherif Basha    09/19/2016   Bug 68359 Sorting of RECIPINET_NAME name should be based on LAST_NAME and FIRST_NAME next
                                   In the upcoming PURLs the p_in_user_id (logged in user) should not be able to contribute for his PURL received
   -- J Flees         10/17/2016   Refactored process design to optimize query effiency.
   --                              Added 'recommended' tab query.
   -- J Flees         11/04/2016   Removed 'query' tab and converted p_in_user_name to a filter for all tabs.
   -- J Flees         01/23/2017   Added department/country list tabs (G6-1460/61).
   -- Arun S          02/21/2017   (G6-1624) Return result set only if record exists in purl_contributor_comment
   -- Arun S          02/22/2017   (G6-1635) Oracle - Avatar enhancements, pull avatar_original instead of avatar_small
   --Ravi Dhanekula   02/27/2017   Removed 'LIKE' option for user_name search as procedure will always get full last_name in p_in_user_name field. Also added missing last name lookup under department and country filters.
   --Gorantla         08/08/2017   JIRA #G6-2773 - Upcoming Celebration and Contribution: The UC Tile is displaying "0 days", even if the purl delivery date is past.
   --Chidamba         08/24/2017   G6-2870 - When a participant becomes inactive, their pending purl should also expire     
   --Gorantla         11/23/2018   Gitlab# 1419 - changes such a way that participant should be able to see new SA details in upcoming celebration page
   --Suresh J         01/22/2019   Bug 78950 - Filter by all search is not returning all the records
   --Gorantla         03/25/2019   Git#1797 The Sorting of columns in Upcoming Celebrations page is incorrect
   --Loganathan       06/03/2019   Bug 79063 - The expired "purl" is showing in the past celebration tile
   --Loganathan       06/25/2019   Bug 79077 - Weird anniversary display issue-A service anniversary year is displayed duplicated 
   								   value in the upcoming celebration tile when user does a department-wise sort out.
   --Gorantla         10/28/2019   Git# 2544 EZ Thanks - UC tile count is not matching with web upcoming celebration count     								   
   *********************************************************************************/
   PRAGMA AUTONOMOUS_TRANSACTION;

   gc_return_code_success        CONSTANT NUMBER
                                             := pkg_const.gc_return_code_success ;
   gc_return_code_failure        CONSTANT NUMBER
                                             := pkg_const.gc_return_code_failure ;

   gc_error                      CONSTANT execution_log.severity%TYPE
                                             := pkg_const.gc_error ;
   gc_info                       CONSTANT execution_log.severity%TYPE
                                             := pkg_const.gc_info ;

   gc_null_id                    CONSTANT NUMBER (18) := pkg_const.gc_null_id;
   gc_search_all_values          CONSTANT VARCHAR2 (30)
                                             := pkg_const.gc_search_all_values ;

   gc_ref_text_country_id        CONSTANT gtt_id_list.ref_text_1%TYPE
                                             := 'country_id' ;
   gc_ref_text_department_type   CONSTANT gtt_id_list.ref_text_1%TYPE
                                             := 'department_type' ;
   gc_ref_text_user_id           CONSTANT gtt_id_list.ref_text_1%TYPE
                                             := 'user_id' ;

   c_process_name                CONSTANT execution_log.process_name%TYPE
      := UPPER ('prc_purl_celebration_page') ;
   c_release_level               CONSTANT execution_log.release_level%TYPE
                                             := 1 ;

   v_stage                                execution_log.text_line%TYPE;
   v_parm_list                            execution_log.text_line%TYPE;
   v_data_sort                            VARCHAR2 (50);
   v_check_days                           os_propertyset.int_val%TYPE;
   v_sa_enabled                           os_propertyset.boolean_val%TYPE;  -- 11/23/2018
BEGIN
   -- initialize variables
   v_stage := 'initialize variables';
   v_parm_list :=
         '~Parms'
      || ': p_in_past_present >'
      || p_in_past_present
      || '<, p_in_tab_type >'
      || p_in_tab_type
      || '<, p_in_list_value >'
      || p_in_list_value
      || '<, p_in_locale >'
      || p_in_locale
      || '<, p_in_user_id >'
      || p_in_user_id
      || '<, p_in_user_name >'
      || p_in_user_name
      || '<, p_in_rownum_start >'
      || p_in_rownum_start
      || '<, p_in_rownum_end >'
      || p_in_rownum_end
      || '<, p_in_sort_by >'
      || p_in_sort_by
      || '<, p_in_sort_on >'
      || p_in_sort_on
      || '<~';           

  -- 11/23/2018
  v_stage := 'To check on new SA enabled';
  SELECT boolean_val
    INTO v_sa_enabled
    FROM os_propertyset
   WHERE entity_name = 'new.service.anniversary.enabled';
   
   -- set data sort
   IF (LOWER (p_in_sort_by) = 'desc')
   THEN
      v_data_sort := LOWER (p_in_sort_by || '/' || p_in_sort_on);
   ELSE
      v_data_sort := LOWER (p_in_sort_on);
   END IF;

   -- build user list based on tab type
   v_stage := 'build user list';

   DELETE gtt_id_list;

   IF (p_in_tab_type = 'team')
   THEN
      INSERT INTO gtt_id_list (id,
                               rec_seq,
                               ref_text_1,
                               ref_text_2,
                               ref_nbr_1)
         (                                                  -- build user list
          SELECT ul.user_id,
                 ROWNUM AS rec_seq,
                 gc_ref_text_user_id AS ref_text_1,
                 NULL AS ref_text_2,
                 1 AS primary_sort
            FROM (                                                -- get users
                  SELECT DISTINCT un_t.user_id
                    FROM user_node un, user_node un_t,                 -- team
                                                      application_user au
                   WHERE     un.user_id = p_in_user_id
                         AND un.status = 1
                         AND un.node_id = un_t.node_id
                         AND un_t.status = 1
                         AND un_t.user_id = au.user_id
                         AND (   p_in_user_name IS NULL
                              OR (    p_in_user_name IS NOT NULL
                                  AND LOWER (au.last_name) = LOWER (p_in_user_name) ))) ul);
   ELSIF (p_in_tab_type = 'followed')
   THEN
      INSERT INTO gtt_id_list (id,
                               rec_seq,
                               ref_text_1,
                               ref_text_2,
                               ref_nbr_1)
         (                                                  -- build user list
          SELECT ul.user_id,
                 ROWNUM AS rec_seq,
                 gc_ref_text_user_id AS ref_text_1,
                 NULL AS ref_text_2,
                 1 AS primary_sort
            FROM (                                                -- get users
                  SELECT DISTINCT pf.follower_id AS user_id
                    FROM participant_followers pf, application_user au
                   WHERE     pf.participant_id = p_in_user_id
                         AND pf.follower_id = au.user_id
                         AND (   p_in_user_name IS NULL
                              OR (    p_in_user_name IS NOT NULL
                                  AND LOWER (au.last_name) = LOWER (p_in_user_name) ))) ul);
   ELSIF (p_in_tab_type = 'recommended')
   THEN
      v_check_days := fnc_get_osp_int ('public.recog.days.check.recommended');

      -- get recommended users first
      INSERT INTO gtt_id_list (id,
                               rec_seq,
                               ref_text_1,
                               ref_text_2,
                               ref_nbr_1)
         (                                                  -- build user list
          SELECT ul.user_id,
                 ROWNUM AS rec_seq,
                 gc_ref_text_user_id AS ref_text_1,
                 NULL AS ref_text_2,
                 1 AS primary_sort
            FROM (                                                -- get users
                  -- get followed
                  SELECT pf.follower_id AS user_id
                    FROM participant_followers pf
                   WHERE pf.participant_id = p_in_user_id
                  UNION
                  -- get team+1 members
                  SELECT un_t1.user_id
                    FROM user_node un_t1
                   WHERE     un_t1.status = 1
                         AND un_t1.node_id IN
                                (                  -- get input user's team(s)
                                 SELECT un.node_id
                                   FROM user_node un
                                  WHERE     un.user_id = p_in_user_id
                                        AND un.status = 1
                                 UNION ALL
                                 -- get 1 level below input user's team(s)
                                 SELECT n.node_id
                                   FROM user_node un, node n
                                  WHERE     un.user_id = p_in_user_id
                                        AND un.status = 1
                                        AND un.node_id = n.parent_node_id)
                  UNION
                  -- get connections
                  SELECT con.user_id
                    FROM (                     -- get recognized by input user
                          SELECT uc.receiver_id AS user_id,
                                 ROW_NUMBER ()
                                 OVER (
                                    ORDER BY uc.date_created DESC, uc.id DESC)
                                    AS rec_seq
                            FROM user_connections uc
                           WHERE uc.sender_id = p_in_user_id
                          UNION ALL
                          -- get recognizers of input user
                          SELECT uc.sender_id AS user_id,
                                 ROW_NUMBER ()
                                 OVER (
                                    ORDER BY uc.date_created DESC, uc.id DESC)
                                    AS rec_seq
                            FROM user_connections uc
                           WHERE uc.receiver_id = p_in_user_id) con
                   -- restrict to the lastest entries
                   WHERE con.rec_seq <= 30) ul,
                 application_user au
           WHERE     ul.user_id = au.user_id
                 AND (   p_in_user_name IS NULL
                      OR (    p_in_user_name IS NOT NULL
                          AND LOWER (au.last_name) = LOWER (p_in_user_name) )));
   -- 01/23/2017 begin block add
   ELSIF (p_in_tab_type = 'department')
   THEN
      pkg_report_common.p_stage_search_criteria (p_in_list_value,
                                                 gc_ref_text_department_type);

      INSERT INTO gtt_id_list (id,
                               rec_seq,
                               ref_text_1,
                               ref_text_2,
                               ref_nbr_1)
         (                                                  -- build user list
          SELECT ul.user_id,
                 ROWNUM AS rec_seq,
                 gc_ref_text_user_id AS ref_text_1,
                 NULL AS ref_text_2,
                 1 AS primary_sort
            FROM (                                                -- get users
                  SELECT pdt.user_id
                    FROM (              -- get participants by department type
                          SELECT pe.user_id,
                                 pe.department_type,
                                 RANK ()
                                 OVER (
                                    PARTITION BY pe.user_id
                                    ORDER BY
                                       pe.termination_date DESC,
                                       pe.participant_employer_index DESC)
                                    AS rec_rank
                            FROM gtt_id_list gil_dt,        -- department type
                                 participant_employer pe_dt, -- department type
                                 participant_employer pe,
                                 application_user au
                           WHERE     gil_dt.ref_text_1 =
                                        gc_ref_text_department_type
                                 AND gil_dt.ref_text_2 =
                                        pe_dt.department_type
                                 AND pe_dt.user_id = pe.user_id
                                 AND pe_dt.PARTICIPANT_EMPLOYER_INDEX = pe.PARTICIPANT_EMPLOYER_INDEX  --06/25/2019   Bug#79077 
                                 AND pe.user_id = au.user_id
                                 AND (   p_in_user_name IS NULL OR (    p_in_user_name IS NOT NULL  AND LOWER (au.last_name) =LOWER (p_in_user_name)))
 ) pdt
                   -- restrict to most recent employer record
                   WHERE pdt.rec_rank = 1) ul);
   ELSIF (p_in_tab_type = 'country')
   THEN
      pkg_report_common.p_stage_search_criteria (p_in_list_value,
                                                 gc_ref_text_country_id,
                                                 1);

      INSERT INTO gtt_id_list (id,
                               rec_seq,
                               ref_text_1,
                               ref_text_2,
                               ref_nbr_1)
         (                                                  -- build user list
          SELECT ul.user_id,
                 ROWNUM AS rec_seq,
                 gc_ref_text_user_id AS ref_text_1,
                 NULL AS ref_text_2,
                 1 AS primary_sort
            FROM (                                                -- get users
                  SELECT ua.user_id
                    FROM gtt_id_list gil_c,                         -- country
                                           user_address ua,
                                           application_user au
                   WHERE     gil_c.ref_text_1 = gc_ref_text_country_id
                         AND gil_c.id = ua.country_id
                         AND ua.user_id = au.user_id
                         AND (   p_in_user_name IS NULL OR (    p_in_user_name IS NOT NULL  AND LOWER (au.last_name) =LOWER (p_in_user_name)))
                         AND ua.is_primary = 1) ul);
   -- 01/23/2017 end block add
   END IF;                                      -- build user list by tab type

   IF (p_in_tab_type IN ('global', 'recommended'))
  -- IF (p_in_tab_type IN ('global'))
   THEN
      --v_check_days := fnc_get_osp_int ('public.recog.days.check');                                                          --01/22/2019  
      v_check_days := CASE WHEN p_in_tab_type='recommended'  THEN fnc_get_osp_int ('public.recog.days.check.recommended')     --01/22/2019 
                           WHEN p_in_tab_type='global'       THEN fnc_get_osp_int ('public.recog.days.check')                 --01/22/2019  
                           ELSE fnc_get_osp_int ('public.recog.days.check.recommended')
                      END;
      

     IF v_sa_enabled = 0 THEN -- 11/23/2018
      -- add global users as secondary
      INSERT INTO gtt_id_list (id,
                               rec_seq,
                               ref_text_1,
                               ref_text_2,
                               ref_nbr_1)
         (                                                  -- build user list
          SELECT ul.user_id,
                 rso.seq_offset + ROWNUM AS rec_seq,
                 gc_ref_text_user_id AS ref_text_1,
                 NULL AS ref_text_2,
                 -- 2 AS primary_sort -- 03/25/2019
                 1 AS primary_sort    -- 03/25/2019
            FROM (                                                -- get users
                  SELECT DISTINCT pr.user_id
                    FROM purl_recipient pr, application_user au
                   WHERE     (   (    p_in_past_present = 'past'
                                  AND pr.award_date >=
                                         TRUNC (SYSDATE) - v_check_days
                                  AND pr.award_date < TRUNC (SYSDATE) + 1)
                              OR (    p_in_past_present = 'upcoming'
                                  AND pr.user_id != p_in_user_id
                                  AND pr.award_date > TRUNC (SYSDATE)))
                         AND pr.user_id = au.user_id
                         AND AU.IS_ACTIVE = 1  --08/24/2017 G6-2870
                         AND (   p_in_user_name IS NULL
                              OR (    p_in_user_name IS NOT NULL
                                  AND LOWER (au.last_name) = LOWER (p_in_user_name)))) ul,
                 (                               -- get record sequence offset
                  SELECT NVL (MAX (gil.rec_seq), 0) AS seq_offset
                    FROM gtt_id_list gil
                   WHERE gil.ref_text_1 = gc_ref_text_user_id) rso
           WHERE     NOT EXISTS
                        (SELECT NULL
                           FROM gtt_id_list gil
                          WHERE     gil.ref_text_1 = gc_ref_text_user_id
                                AND gil.id = ul.user_id)
                 AND ROWNUM < 200);
      ELSIF v_sa_enabled = 1 THEN  -- 11/23/2018 start
          -- add global users as secondary
      INSERT INTO gtt_id_list (id,
                               rec_seq,
                               ref_text_1,
                               ref_text_2,
                               ref_nbr_1)
         (                                                  -- build user list
          SELECT ul.user_id,
                 rso.seq_offset + ROWNUM AS rec_seq,
                 gc_ref_text_user_id AS ref_text_1,
                 NULL AS ref_text_2,
                 2 AS primary_sort
            FROM (                                                -- get users
                  SELECT DISTINCT pr.recipient_id user_id
                    FROM sa_celebration_info pr, application_user au
                   WHERE     (   (    p_in_past_present = 'past'
                                  AND pr.award_status <> 'Scheduled'
                                  AND pr.award_date >=
                                         TRUNC (SYSDATE) - v_check_days
                                  AND pr.award_date < TRUNC (SYSDATE) + 1)
                              OR (    p_in_past_present = 'upcoming'
                                  AND pr.award_status = 'Scheduled'
                                  AND pr.recipient_id != p_in_user_id
                                  AND pr.award_date > TRUNC (SYSDATE)))
                         AND pr.recipient_id = au.user_id
                         AND AU.IS_ACTIVE = 1  --08/24/2017 G6-2870
                         AND (   p_in_user_name IS NULL
                              OR (    p_in_user_name IS NOT NULL
                                  AND LOWER (au.last_name) = LOWER (p_in_user_name)))) ul,
                 (                               -- get record sequence offset
                  SELECT NVL (MAX (gil.rec_seq), 0) AS seq_offset
                    FROM gtt_id_list gil
                   WHERE gil.ref_text_1 = gc_ref_text_user_id) rso
           WHERE     NOT EXISTS
                        (SELECT NULL
                           FROM gtt_id_list gil
                          WHERE     gil.ref_text_1 = gc_ref_text_user_id
                                AND gil.id = ul.user_id)
                 AND ROWNUM < 200);
      END IF; -- 11/23/2018 end
   END IF;                                                           -- global

   v_stage := 'OPEN p_out_result_set';
   IF v_sa_enabled = 0 THEN -- 11/23/2018
   OPEN p_out_result_set FOR
        SELECT sd.*
          FROM (                                              -- sequence data
                SELECT rd.user_id,
                       rd.recipient_id,
                       rd.last_name,
                       rd.first_name,
                       rd.anniversary,
                       null cmx_asset_code, -- 11/23/2018
                       --rd.avatar_small,     --02/22/2017
                       rd.avatar_original avatar_small,           --02/22/2017
                       null position_type,  -- 11/23/2018
                       null primary_color,  -- 11/23/2018
                       null secondary_color,  -- 11/23/2018
                       null celebration_id, -- 11/23/2018
                       rd.promotion_id,
                       rd.promotion_name,
                       null prog_cmx_value,  -- 11/23/2018
                       rd.expiration_date,
                       COUNT (rd.recipient_id) OVER () AS total_records,
                       -- calc record sort order
                       ROW_NUMBER ()
                       OVER (
                          ORDER BY
                             -- sort on primary sort
                             rd.primary_sort,
                             -- ascending sorts
                             DECODE (v_data_sort,
                                     'promotion_name', LOWER (promotion_name)) ASC NULLS LAST,
                             DECODE (v_data_sort,
                                     'expiration_date', rd.expiration_date) ASC NULLS LAST,
                             DECODE (
                                v_data_sort,
                                'anniversary', TO_NUMBER (
                                                  REGEXP_SUBSTR (
                                                     rd.anniversary,
                                                     '[[:digit:]]*'))) ASC NULLS LAST,
                             DECODE (
                                v_data_sort,
                                'recipient_name', LOWER (
                                                        rd.last_name
                                                     || rd.first_name)) ASC NULLS LAST,
                             -- secondary expiration date sort
                             DECODE (
                                v_data_sort,
                                'expiration_date', LOWER (
                                                         rd.last_name
                                                      || rd.first_name)) ASC NULLS LAST,
                             -- descending sorts
                             DECODE (
                                v_data_sort,
                                'desc/promotion_name', LOWER (promotion_name)) DESC NULLS LAST,
                             DECODE (
                                v_data_sort,
                                'desc/expiration_date', rd.expiration_date) DESC NULLS LAST,
                             DECODE (
                                v_data_sort,
                                'desc/anniversary', TO_NUMBER (
                                                       REGEXP_SUBSTR (
                                                          rd.anniversary,
                                                          '[[:digit:]]*'))) DESC NULLS LAST,
                             DECODE (
                                v_data_sort,
                                'desc/recipient_name', LOWER (
                                                             rd.last_name
                                                          || rd.first_name)) DESC NULLS LAST,
                             -- secondary expiration date sort
                             DECODE (
                                v_data_sort,
                                'desc/expiration_date', LOWER (
                                                              rd.last_name
                                                           || rd.first_name)) DESC NULLS LAST,
                             -- default sort
                             rd.recipient_id)
                          AS rec_seq
                  FROM (                                     -- get result set
                        SELECT   pr.user_id,
                                 pr.purl_recipient_id AS recipient_id,
                                 au.last_name,
                                 au.first_name,
                                 LISTAGG (
                                    prc.VALUE,
                                    ' ')
                                 WITHIN GROUP (ORDER BY
                                                  prc.purl_recipient_cfse_id)
                                    AS anniversary,
                                 --pax.avatar_small,      --02/22/2017
                                 pax.avatar_original,             --02/22/2017
                                 pm.promotion_id,
                                 cav.cms_value AS promotion_name,
                                 pr.award_date AS expiration_date,
                                 gil.ref_nbr_1 AS primary_sort
                            FROM gtt_id_list gil,
                                 purl_recipient pr,
                                 purl_recipient_cfse prc,
                                 application_user au,
                                 participant pax,
                                 promotion pm,
                                 promo_recognition pmr,
                                 vw_cms_asset_value cav
                           WHERE     gil.ref_text_1 = gc_ref_text_user_id
                                 -- restrict recipient list
                                 AND gil.id = pr.user_id
                                 AND (   (    p_in_past_present = 'past'
                                          AND pr.award_date < TRUNC (SYSDATE) + 1
                                          AND PR.STATE NOT IN ('expired','archived')) --06/03/2019  Bug#79063
                                      OR (    p_in_past_present = 'upcoming'
                                          AND pr.user_id != p_in_user_id
                                          AND trunc(pr.award_date) > TRUNC (SYSDATE)))  -- 08/08/2017
                                 AND pr.purl_recipient_id =
                                        prc.purl_recipient_id(+)
                                 AND pr.user_id = pax.user_id(+)
                                 AND pr.user_id = au.user_id --(+)  --08/24/2017 G6-2870 
                                 AND au.is_active = 1 --08/24/2017 G6-2870
                                 AND pr.promotion_id = pm.promotion_id
                                 AND pm.promo_name_asset_code = cav.asset_code
                                 AND pm.promotion_id = pmr.promotion_id
                                 AND pmr.display_purl_in_purl_tile = 1
                                 AND cav.locale = p_in_locale
                                 AND (   p_in_past_present = 'upcoming'
                                      OR EXISTS
                                            (            --added on 02/21/2017
                                             SELECT 'X'
                                               FROM purl_contributor pc,
                                                    purl_contributor_comment pcc
                                              WHERE     pc.purl_contributor_id =
                                                           pcc.purl_contributor_id
                                                    AND pc.purl_recipient_id =
                                                           pr.purl_recipient_id))
                        GROUP BY pr.user_id,
                                 pr.purl_recipient_id,
                                 au.last_name,
                                 au.first_name,
                                 --pax.avatar_small,      --02/22/2017
                                 pax.avatar_original,             --02/22/2017
                                 pm.promotion_id,
                                 cav.cms_value,
                                 pr.award_date,
                                 gil.ref_nbr_1) rd) sd
         WHERE sd.rec_seq BETWEEN p_in_rownum_start AND p_in_rownum_end
      ORDER BY sd.rec_seq;
     ELSIF v_sa_enabled = 1 THEN  -- 11/23/2018 start
       OPEN p_out_result_set FOR
    SELECT sd.*
      FROM (                                              -- sequence data
            SELECT rd.user_id,
                   null recipient_id,
                   rd.last_name,
                   rd.first_name,
                   rd.anniversary,
                   rd.celeb_msg_cmx_asset_code cmx_asset_code,
                   rd.avatar_original avatar_small,           --02/22/2017
                   rd.position_type,
                   rd.primary_color,
                   rd.secondary_color,
                   rd.celebration_id,
                   rd.promotion_id,
                   rd.promotion_name,
                   rd.prog_cmx_value,
                   rd.expiration_date,
                   COUNT (rd.user_id) OVER () AS total_records,
                   -- calc record sort order
                   ROW_NUMBER ()
                   OVER (
                      ORDER BY
                         -- sort on primary sort
                         rd.primary_sort,
                         -- ascending sorts
                         DECODE (v_data_sort,
                                 'promotion_name', LOWER (promotion_name)) ASC NULLS LAST,
                         DECODE (v_data_sort,
                                 'expiration_date', rd.expiration_date) ASC NULLS LAST,
                         DECODE (
                            v_data_sort,
                            'anniversary', rd.anniversary) ASC NULLS LAST,
                         DECODE (
                            v_data_sort,
                            'recipient_name', LOWER (
                                                    rd.last_name
                                                 || rd.first_name)) ASC NULLS LAST,
                         -- secondary expiration date sort
                         DECODE (
                            v_data_sort,
                            'expiration_date', LOWER (
                                                     rd.last_name
                                                  || rd.first_name)) ASC NULLS LAST,
                         -- descending sorts
                         DECODE (
                            v_data_sort,
                            'desc/promotion_name', LOWER (promotion_name)) DESC NULLS LAST,
                         DECODE (
                            v_data_sort,
                            'desc/expiration_date', rd.expiration_date) DESC NULLS LAST,
                         DECODE (
                            v_data_sort,
                            'desc/anniversary', rd.anniversary) DESC NULLS LAST,
                         DECODE (
                            v_data_sort,
                            'desc/recipient_name', LOWER (
                                                         rd.last_name
                                                      || rd.first_name)) DESC NULLS LAST,
                         -- secondary expiration date sort
                         DECODE (
                            v_data_sort,
                            'desc/expiration_date', LOWER (
                                                          rd.last_name
                                                       || rd.first_name)) DESC NULLS LAST,
                         -- default sort
                         rd.user_id)
                      AS rec_seq
              FROM (                                     -- get result set
                     SELECT  pr.recipient_id user_id,
                             au.last_name,
                             au.first_name,
                             pwl.celeb_msg anniversary,
                             pwl.celeb_msg_cmx_asset_code ,
                             pax.avatar_original, 
                             pe.position_type,
                             pm.primary_color,
                             pm.secondary_color,
                             pr.celebration_id,
                             pm.program_id promotion_id,
                             pm.program_name AS promotion_name,
                             pm.program_name_cmx_asset_code prog_cmx_value,
                             pr.award_date AS expiration_date,
                             gil.ref_nbr_1 AS primary_sort
                        FROM gtt_id_list gil,
                             sa_celebration_info pr,
                             application_user au,
                             participant pax,
                             program pm,
                             program_award_level pwl,
                             vw_curr_pax_employer pe,
                             (SELECT ua.country_id,
                                       ua.user_id,
                                       c.awardbanq_country_abbrev 
                                FROM user_address ua, 
                                     country c 
                                WHERE c.country_id=ua.country_id and ua.is_primary=1
                                ) cy 
                       WHERE  gil.ref_text_1 = gc_ref_text_user_id
                             -- restrict recipient list
                             AND gil.id = pr.recipient_id
                             AND (   (   p_in_past_present = 'past'
                                      AND pr.award_status <> 'Scheduled'
                                      AND pr.award_date <  (SYSDATE) + 1)
                                  OR (    p_in_past_present = 'upcoming'
                                      AND pr.recipient_id != p_in_user_id
                                      AND pr.award_status = 'Scheduled'
                                      AND pm.allow_contribution = 1
                                      AND (pr.award_date) >  (SYSDATE))) 
                             AND pr.recipient_id = pax.user_id(+)
                             AND pr.recipient_id = au.user_id 
                             AND au.is_active = 1 
                             AND pr.program_id = pm.program_id
                             AND pm.program_id = pwl.program_id
                             AND pwl.award_level = pr.award_level
                             and pr.recipient_id = cy.user_id (+)
                             AND pwl.program_id = pr.program_id
                             AND pwl.country = cy.awardbanq_country_abbrev
                             --AND pm.allow_contribution = 1
                             and au.user_id = pe.user_id(+) -- 10/28/2019            
                    GROUP BY pr.recipient_id,
                             au.last_name,
                             au.first_name,
                             pax.avatar_original,
                             pwl.celeb_msg,
                             pwl.celeb_msg_cmx_asset_code,
                             pe.position_type,
                             pm.primary_color,
                             pm.secondary_color,
                             pr.celebration_id,
                             pm.program_id,
                             pm.program_name,
                             pm.program_name_cmx_asset_code,
                             pr.award_date,
                             gil.ref_nbr_1) rd) sd
         WHERE sd.rec_seq BETWEEN p_in_rownum_start AND p_in_rownum_end
      ORDER BY sd.rec_seq;
     END IF; -- 11/23/2018 end
   -- successful
   p_out_returncode := gc_return_code_success;

   COMMIT;                                  -- release DML locks on temp table
   
EXCEPTION
   WHEN OTHERS
   THEN
      prc_execution_log_entry (
         c_process_name,
         c_release_level,
         gc_error,
         v_stage || v_parm_list || SQLCODE || ':' || SQLERRM,
         NULL);
      ROLLBACK;                             -- release DML locks on temp table
      p_out_returncode := gc_return_code_failure;

      OPEN p_out_result_set FOR
         SELECT NULL
           FROM DUAL;
END prc_purl_celebration_page;
/
