CREATE OR REPLACE PROCEDURE prc_get_user_info
                            (p_in_user_ids IN VARCHAR2,
                             p_out_retun_code OUT NUMBER,
                             p_out_data                  OUT SYS_REFCURSOR,
                             p_out_path                  OUT SYS_REFCURSOR)
IS
/***************************************************************
Purpose : To provide user info for indexing the audiences. 

Author            Date                 Comments
------          -------              -----------
Ravi Dhanekula 07/01/2016           -Initial Creation
Arun S         02/22/2017           (G6-1635) Oracle - Avatar enhancements, pull avatar_original instead of avatar_small
Ravi Dhanekula 04/04/2017            Added is_opt_out_of_awards column to result set.
Ravi Dhanekula 05/05/2017            G6-2327 Showing inactive pax in search.
Gorantla       11/15/2018            Gitlab#1400  Added extra fields to the result set.
Gorantla       10/21/2019            Gitlab#2518  Added extra field rosterpersonid to the result set.
Gorantla       10/28/2019            Gitlab#2518  modified to get date format as 'MM/DD/YYYY' and added two new columns
Gorantla       12/01/2019            Gitlab#2611  Add UUID columns and modify the oracle packages to insert UUID.
*****************************************************************/

BEGIN

    OPEN p_out_data FOR
       SELECT user_id,
             roster_user_id, -- 10/21/2019  -- 12/01/2019 replaced rosterpersonid
             first_name,
             last_name,
             position_type,
             department_type,
             is_active,
             node_id,
             country_id,
             avatar_url,
             LISTAGG (audience_id, ',') WITHIN GROUP (ORDER BY 1) AS audience_ids,
             non_primary_node_ids,
             -- 11/15/2018 start
             middle_name,
             title,
             suffix,
             to_char(hire_date,'MM/DD/YYYY') hire_date,  -- 10/28/2019
             to_char(termination_date,'MM/DD/YYYY') termination_date, -- 10/28/2019
             user_name,
             gender,
             CASE WHEN gender IS NOT NULL THEN fnc_cms_asset_code_val_extr('roster.compliance.pronouns.info',DECODE(gender,'m','PRONOUNS_HE','f','PRONOUNS_SHE'),language_id)
              ELSE null END objective,  -- 10/28/2019
             CASE WHEN gender IS NOT NULL THEN fnc_cms_asset_code_val_extr('roster.compliance.pronouns.info',DECODE(gender,'m','PRONOUNS_HIM','f','PRONOUNS_HER'),language_id)
              ELSE null END subjective, -- 10/28/2019
             role role_type,
             language_id languagePreference,
             state,
             person_addr_country,
             awardbanq_country_abbrev person_country,
             person_address,  
             email_address,
             person_phones,
             person_chars,
             -- 11/15/2018 end
             is_opt_out_of_awards
        FROM (WITH v_participant_employer
                   AS (SELECT -- get most recent participant employer record per user
                             r."REC_RANK",
                              r."USER_ID",
                              r."POSITION_TYPE",
                              r."DEPARTMENT_TYPE",
                              r."TERMINATION_DATE",
                              r."HIRE_DATE"
                         FROM ( -- rank records by termination date and employer index in reverse order
                               SELECT RANK ()
                                      OVER (
                                         PARTITION BY pe.user_id
                                         ORDER BY
                                            pe.termination_date DESC,
                                            pe.participant_employer_index DESC)
                                         AS rec_rank,
                                      pe.*
                                 FROM participant_employer pe
                                WHERE user_id IN (SELECT *
                                                    FROM TABLE (
                                                            get_array (
                                                               p_in_user_ids)))) r
                        -- the current employment record has the lowest ranking
                        WHERE r.rec_rank = 1)
              SELECT au.user_id,
                     au.roster_user_id, -- 10/21/2019  -- 12/01/2019 replaced rosterpersonid
                     au.first_name,
                     au.last_name,
                     vw.position_type,
                     vw.department_type,
--                     DECODE(p.status,'inactive',0,1) is_active,--05/05/2017
                     CASE --WHEN P.terms_acceptance ='declined' THEN 0
                          WHEN p.status = 'inactive' THEN 0
                          ELSE 1 END is_active,--05/05/2017
                     un.node_id,
                     ua.country_id,
                     --p.avatar_small   avatar_url,    --02/22/2017
                     p.avatar_original  avatar_url,    --02/22/2017
                     p.is_opt_out_of_awards,
                     audience_id,
                     (SELECT LISTAGG (node_id, ',') WITHIN GROUP (ORDER BY 1)
                                AS non_primary_node_ids
                        FROM user_node
                       WHERE status = 1 AND is_primary = 0 AND user_id = p.user_id)
                        non_primary_node_ids,
                      -- 11/15/2018 start
                      au.user_name,
                      middle_name,
                      au.title,
                      au.suffix,
                      vw.hire_date,
                      vw.termination_date,
                      au.gender,
                      au.language_id,
                      un.role,
                      ua.state,
                      c.awardbanq_country_abbrev,
                     (SELECT LISTAGG(address_type || '|' ||c.awardbanq_country_abbrev || '|' || addr1 || '|' || addr2 || '|' || addr3 || '|' ||addr4
                             || '|' ||addr5 || '|' ||addr6 || '|' ||city || '|' || state || '|' || postal_code || '|' || is_primary ,'; ')
                             WITHIN GROUP ( ORDER BY address_type)
                                AS person_address
                        FROM user_address ua,
                             country c
                       WHERE user_id = p.user_id 
                         AND ua.country_id = c.country_id)
                        person_address,
                        (SELECT LISTAGG(awardbanq_country_abbrev,',') WITHIN GROUP ( ORDER BY address_type) person_addr_country
                        FROM user_address ua,
                             country c
                       WHERE ua.country_id = c.country_id AND is_primary = 0 AND user_id = p.user_id )
                        person_addr_country,
                      (SELECT LISTAGG(email_type || '|' || email_addr || '|' || is_primary ,'; ')
                              WITHIN GROUP ( ORDER BY email_type)
                                AS email_address
                        FROM user_email_address
                       WHERE user_id = p.user_id)
                        email_address,
                       (SELECT LISTAGG(phone_type || '|' || phone_nbr || '|' || country_phone_code|| '|' || is_primary ,'; ')
                              WITHIN GROUP ( ORDER BY phone_type)
                                AS person_phones
                        FROM user_phone
                       WHERE user_id = p.user_id)
                        person_phones,
                        (SELECT LISTAGG(fnc_cms_asset_code_value(cm_asset_code)|| '|' || characteristic_value ,'; ')
                                WITHIN GROUP ( ORDER BY 1) 
                                  AS person_chars
                           FROM user_characteristic uc,
                                characteristic c
                          WHERE c.characteristic_id = uc.characteristic_id
                            AND user_id = p.user_id)
                           person_chars
                          -- 11/15/2018 end
                FROM application_user au,
                     participant p,
                     v_participant_employer vw,
                     participant_audience pa,
                     user_address ua,
                     country c,  -- 11/15/2018
                     user_node un
               WHERE     au.user_id IN (SELECT *
                                          FROM TABLE (get_array (p_in_user_ids)))
                     AND au.user_id = p.user_id
                     AND au.user_id = un.user_id
                     AND un.is_primary = 1
--                     AND un.status = 1
                     AND au.user_id = ua.user_id
                     AND ua.is_primary = 1
                     AND ua.country_id = c.country_id -- 11/15/2018
                     AND au.user_id = vw.user_id(+)
                     AND au.user_id = pa.user_id(+))
    GROUP BY user_id,
             roster_user_id, -- 10/21/2019  -- 12/01/2019 replaced rosterpersonid
             first_name,
             last_name,
             position_type,
             department_type,
             is_active,
             node_id,
             country_id,
             avatar_url,
             non_primary_node_ids,
             is_opt_out_of_awards,
             -- 11/15/2018 start
             middle_name,
             title,
             suffix,
             hire_date,
             termination_date,
             user_name,
             gender,
             role,
             language_id,
             state,
             person_addr_country,
             awardbanq_country_abbrev,
             person_address,  
             email_address,
             person_phones,
             person_chars;
             -- 11/15/2018 end

              
    OPEN p_out_path FOR
        WITH org_unit_path
             AS (    SELECT n.node_id, SYS_CONNECT_BY_PATH (node_id, '/') PATH
                       FROM node n
                 START WITH n.parent_node_id IS NULL
                 CONNECT BY PRIOR n.node_id = n.parent_node_id)
        SELECT DISTINCT un.user_id, oup.PATH
          FROM org_unit_path oup, user_node un
         WHERE     un.user_id IN (SELECT * FROM TABLE (get_array (p_in_user_ids)))
               AND un.node_id = oup.node_id;
                     
  p_out_retun_code :=0;

EXCEPTION WHEN OTHERS THEN
  p_out_retun_code :=99;
  OPEN p_out_data FOR SELECT NULL FROM DUAL;
  OPEN p_out_path FOR SELECT NULL FROM DUAL;
END;
/