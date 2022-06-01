CREATE OR REPLACE PROCEDURE prc_award_generator (
   p_award_generator_id         IN     NUMBER,
   p_award_generator_batch_id   IN OUT     NUMBER,
   p_batch_start_date           IN     DATE,
   p_batch_end_date             IN     DATE,
   p_use_issue_date             IN     NUMBER,--0/1 If it is '0' use anniversary date to popultae issue-date in awardgen_participant table.
   p_issue_date                 IN     DATE,
   p_award_type                 IN     VARCHAR2, 
   p_out_notify_manager OUT NUMBER,
   p_out_return_code               OUT NUMBER)
AS
 /*******************************************************************************
  -- Purpose: To Process award file load generator data to insert data into awardgen_participant and awardgen_manager tables.
  --
  -- Person                  Date       Comments
  -- -----------            --------   -----------------------------------------------------
  -- Ravi Dhanekula        08/19/2015  Creation
  -- nagarajs              10/23/2015  Bug 64206 - Award file Generator: Application errors out, when trying to save 
                                       in 'Update Award File Setup' for the promotion with Plateau awards
 -- nagarajs               11/18/2015  Bug 64668 - Award File Generator: File not getting generated in email for the promotion with plateau 
 -- nagarajs              01/21/2016   Bug 65178 - Application Error - Award File Generator 
 --Ravi Arumugam          05/10/2016  Bug 66740 - Award file generator is sending out emails for every year when promotion is not set up for every year
 --Suresh J               02/04/2019  Bug 77279 - Award File Generator - Hitting launch button for the pax Hire date feb 29 throws application error
 --Suresh J               02/22/2019  Bug 77279 - Award File Generator - Hitting launch button for the pax Birth date feb 29 throws application error
 --Loganathan 			  04/12/2019  Bug 79012 - Award File generator creates duplicate records for plateau file
 --Loganathan 			  05/19/2019  Bug 79012 - Award File generator creates duplicate records for plateau file
                                                  (During points promotion level id is returning Null)
 --Loganathan             05/22/2019  Bug 79012 - Award File generator creates duplicate records for plateau file (Duplicate fix for characteristics as well)                       
  *******************************************************************************/
   v_stage            VARCHAR2 (300);
   c_created_by       NUMBER := 0;
   v_examiner_field   VARCHAR2 (256);
   v_promotion_id     NUMBER (16);
   v_award_amount     NUMBER (16);
   v_expiry_date      DATE;
   v_batch_id          NUMBER;
   v_notify_manager NUMBER;
   v_characteristic_id NUMBER;
BEGIN
   prc_execution_log_entry ('PRC_AWARD_GENERATOR',
                            1,
                            'INFO',
                            'Process Started '||p_award_generator_id ||','||
   p_award_generator_batch_id||','||
   p_batch_start_date ||','||
   p_batch_end_date||','||
   p_use_issue_date||','||
   p_issue_date ,
                            NULL);

   SELECT examiner_field, promotion_id, (SYSDATE + number_of_days_for_alert),notify_manager
     INTO v_examiner_field, v_promotion_id, v_expiry_date,v_notify_manager
     FROM awardgenerator
    WHERE awardgen_id = p_award_generator_id;

  /*IF LOWER(p_award_type) = 'points' THEN  --10/23/2015 --01/21/2016
   SELECT award_amount
     INTO v_award_amount
     FROM awardgen_award
    WHERE awardgen_id = p_award_generator_id;
  END IF;*/
   v_stage := 'Populate AWARDGEN_BATCH table';

   IF p_award_generator_batch_id IS NULL
   THEN
   SELECT AWARDGEN_BATCH_PK_SQ.NEXTVAL INTO v_batch_id FROM DUAL;
      INSERT INTO awardgen_batch (awardgen_batch_id,
                                  awardgen_id,
                                  start_date,
                                  end_date,
                                  use_issue_date,
                                  issue_date,
                                  created_by,
                                  date_created,
                                  version)
           VALUES (v_batch_id,
                   p_award_generator_id,
                   p_batch_start_date,
                   p_batch_end_date,
                   0,
                   NULL,
                   c_created_by,
                   SYSDATE,
                   0);
   ELSE
   
   DELETE FROM awardgen_manager WHERE awardgen_batch_id = p_award_generator_batch_id;
   DELETE FROM awardgen_participant WHERE awardgen_batch_id = p_award_generator_batch_id;
   
                   
   END IF;

   v_stage := 'Populate Awardgen_participant table';

   IF v_examiner_field = 'Birth Date'
   THEN
   prc_execution_log_entry ('PRC_AWARD_GENERATOR',
                            1,
                            'INFO',
                            'INSIDE BIRTH_DATE '||
                           p_award_generator_id ||',v_examiner_field->'||v_examiner_field||
                           p_award_generator_batch_id||','||
                           p_batch_start_date ||','||
                           p_batch_end_date||','||
                           p_use_issue_date||','||
                           p_issue_date ,
                                                    NULL);

      INSERT INTO AWARDGEN_PARTICIPANT (awardgen_participant_id,
                                        awardgen_batch_id,
                                        user_id,
                                        anniversary_num_days,
                                        award_amount,
                                        anniversary_date,
                                        anniversary_num_years,
                                        level_id,   --11/18/2015
                                        date_created,
                                        created_by,
                                        version)
         (SELECT AWARDGEN_PARTICIPANT_PK_SQ.NEXTVAL,
                 res.batch_id,          --01/21/2016
                 res.user_id,           --01/21/2016
                 res.days,              --01/21/2016
                 award_amount,          --01/21/2016 v_award_amout
                 TO_DATE(res.anniversaryDate,'MM/DD/YYYY'), --01/21/2016
                 res.years,             --01/21/2016
                 res.level_id,   --11/18/2015--01/21/2016
                 SYSDATE,
                 c_created_by,
                 0 FROM (
                 SELECT 
                 NVL(p_award_generator_batch_id,v_batch_id) batch_id,
                 p_award_generator_id awardgen_id,          --01/21/2016
                 au.user_id,
                 aw.level_id,   --11/18/2015
                 TRUNC (SYSDATE) - TRUNC (au.birth_date) AS days,                
--                    TO_CHAR (TRUNC (au.birth_date), 'MM/DD')                  --02/22/2019
--                 || '/'
--                 || CASE
--                       WHEN TO_CHAR (p_batch_start_date, 'YYYY') =
--                               TO_CHAR (p_batch_end_date, 'YYYY')
--                       THEN
--                          TO_CHAR (TRUNC (p_batch_start_date), 'YYYY')
--                       WHEN     TO_CHAR (p_batch_start_date, 'YYYY') !=
--                                   TO_CHAR (p_batch_end_date, 'YYYY')
--                            AND TO_CHAR (birth_date, 'MMDD') BETWEEN TO_CHAR (
--                                                                        p_batch_start_date,
--                                                                        'MMDD')
--                                                                 AND '1232'
--                       THEN
--                          TO_CHAR (TRUNC (p_batch_start_date), 'YYYY')
--                       WHEN     TO_CHAR (p_batch_start_date, 'YYYY') !=
--                                   TO_CHAR (p_batch_end_date, 'YYYY')
--                            AND TO_CHAR (birth_date, 'MMDD') BETWEEN '0100'
--                                                                 AND TO_CHAR (
--                                                                        p_batch_end_date,
--                                                                        'MMDD')
--                       THEN
--                          TO_CHAR (TRUNC (p_batch_end_date), 'YYYY')
--                    END
--                    AS anniversaryDate,
                        CASE WHEN MOD(TO_NUMBER(TO_CHAR(p_batch_start_date,'YYYY')),4)=0 AND        --02/22/2019
                                  MOD(TO_NUMBER(TO_CHAR(p_batch_start_date,'YYYY')),100)=0 AND 
                                  MOD(TO_NUMBER(TO_CHAR(p_batch_start_date,'YYYY')),400)=0 AND
                                  TO_CHAR (TRUNC (au.birth_date), 'MMDD')='0229'                                  
                                  THEN '02/29/'||TO_CHAR(p_batch_start_date,'YYYY')
                             WHEN MOD(TO_NUMBER(TO_CHAR(p_batch_start_date,'YYYY')),4)=0 AND 
                                  MOD(TO_NUMBER(TO_CHAR(p_batch_start_date,'YYYY')),100)>0 AND
                                  TO_CHAR (TRUNC (au.birth_date), 'MMDD')='0229'                                  
                                  THEN '02/29/'||TO_CHAR(p_batch_start_date,'YYYY')
                             WHEN TO_CHAR (TRUNC (au.birth_date), 'MMDD')='0229' AND
                                  MOD(TO_NUMBER(TO_CHAR(p_batch_start_date,'YYYY')),4)>0                                                                 
                                  THEN '02/28/'||TO_CHAR(p_batch_start_date,'YYYY')
                             ELSE 
                                 TO_CHAR (TRUNC (au.birth_date), 'MM/DD')
                                 || '/'
                                 || CASE
                                       WHEN TO_CHAR (p_batch_start_date, 'YYYY') = TO_CHAR (p_batch_end_date, 'YYYY')
                                       THEN
                                            TO_CHAR (TRUNC (p_batch_start_date), 'YYYY')
                                       WHEN TO_CHAR (p_batch_start_date, 'YYYY') != TO_CHAR (p_batch_end_date, 'YYYY')
                                                AND TO_CHAR (au.birth_date, 'MMDD') BETWEEN TO_CHAR (p_batch_start_date,'MMDD') AND '1232'
                                       THEN
                                            TO_CHAR (TRUNC (p_batch_start_date), 'YYYY')
                                       WHEN TO_CHAR (p_batch_start_date, 'YYYY') != TO_CHAR (p_batch_end_date, 'YYYY')
                                            AND TO_CHAR (au.birth_date, 'MMDD') BETWEEN '0100' AND TO_CHAR ( p_batch_end_date,'MMDD')
                                       THEN
                                          TO_CHAR (TRUNC (p_batch_end_date), 'YYYY')
                                    END
                          END
                    AS anniversaryDate,
                 CASE
                    WHEN TO_CHAR (p_batch_start_date, 'YYYY') =
                            TO_CHAR (p_batch_end_date, 'YYYY')
                    THEN
                       (  TO_CHAR (TRUNC (p_batch_end_date), 'YYYY')
                        - TO_CHAR (TRUNC (au.birth_date), 'YYYY'))
                    WHEN     TO_CHAR (p_batch_start_date, 'YYYY') !=
                                TO_CHAR (p_batch_end_date, 'YYYY')
                         AND TO_CHAR (birth_date, 'MMDD') BETWEEN TO_CHAR (
                                                                     p_batch_start_date,
                                                                     'MMDD')
                                                              AND '1232'
                    THEN
                       (  TO_CHAR (TRUNC (p_batch_start_date), 'YYYY')
                        - TO_CHAR (TRUNC (au.birth_date), 'YYYY'))
                    WHEN     TO_CHAR (p_batch_start_date, 'YYYY') !=
                                TO_CHAR (p_batch_end_date, 'YYYY')
                         AND TO_CHAR (birth_date, 'MMDD') BETWEEN '0100'
                                                              AND TO_CHAR (
                                                                     p_batch_end_date,
                                                                     'MMDD')
                    THEN
                       (  TO_CHAR (TRUNC (p_batch_end_date), 'YYYY')
                        - TO_CHAR (TRUNC (au.birth_date), 'YYYY'))
                 END
                    AS years                
            FROM application_user au, participant p,
                  ( SELECT ua.user_id, aw.level_id      --11/18/2015
                      FROM awardgen_award aw,            
                           promo_merch_program_level pm, 
                           promo_merch_country pc,       
                           user_address ua              
                     WHERE aw.awardgen_id = p_award_generator_id              
                       AND aw.level_id = pm.promo_merch_program_level_id           
                       AND pm.promo_merch_country_id = pc.promo_merch_country_id  
                       AND pc.country_id = ua.country_id                           
                       AND ua.is_primary  = 1 ) aw
           WHERE     au.user_id = p.user_id
                 AND p.status = 'active'
                 AND p.user_id = aw.user_id(+)                                  --11/18/2015
                 AND (   (    TO_CHAR (p_batch_start_date, 'YYYY') =
                                 TO_CHAR (p_batch_end_date, 'YYYY')
                          AND TO_CHAR (birth_date, 'MM/DD') BETWEEN TO_CHAR (
                                                                       p_batch_start_date,
                                                                       'MM/DD')
                                                                AND TO_CHAR (
                                                                       p_batch_end_date,
                                                                       'MM/DD'))
                      OR (    TO_CHAR (p_batch_start_date, 'YYYY') !=
                                 TO_CHAR (p_batch_end_date, 'YYYY')
                          AND (   TO_CHAR (birth_date, 'MMDD') BETWEEN TO_CHAR (
                                                                          p_batch_start_date,
                                                                          'MMDD')
                                                                   AND '1232'
                               OR TO_CHAR (birth_date, 'MMDD') BETWEEN '0100'
                                                                   AND TO_CHAR (
                                                                          p_batch_end_date,
                                                                          'MMDD'))))
                 AND (   EXISTS
                            (SELECT *
                               FROM PROMOTION
                              WHERE     promotion_id = v_promotion_id
                                    AND (CASE
                                           WHEN secondary_audience_type =
                                                   'sameasprimaryaudience'
                                           THEN
                                              primary_audience_type
                                           ELSE
                                              secondary_audience_type
                                        --END = 'allactivepaxaudience')   			           --05/22/2019 Bug 79012 Commented
                                        END) IN ('allactivepaxaudience','specifyaudience'))		--05/22/2019 Bug 79012
                      OR EXISTS
                            (SELECT p.promotion_id
                               FROM promo_audience pa,
                                    promotion p,
                                    participant_audience pax
                              WHERE     pax.user_id = au.user_id
                                    AND pax.audience_id = pa.audience_id
                                    AND pa.promotion_id = p.promotion_id
                                    AND pa.promo_audience_type = 'SECONDARY'
                                    AND p.promotion_id = v_promotion_id)))res,
              awardgen_award awa                                    --01/21/2016
        WHERE res.awardgen_id = awa.awardgen_id  --(+)                 --01/21/2016 removed(+) --05/10/2016 Bug#66740
          AND res.years = awa.years              --(+)                              removed(+) --05/10/2016 Bug#66740
          and NVL(res.level_id,0)=NVL(awa.LEVEL_ID,0)				--05/22/2019 Bug#79012
         );                            --01/21/2016
   ELSIF v_examiner_field = 'Hire Date' 
   THEN
      INSERT INTO AWARDGEN_PARTICIPANT (awardgen_participant_id,
                                        awardgen_batch_id,
                                        user_id,
                                        anniversary_num_days,
                                        award_amount,
                                        anniversary_date,
                                        anniversary_num_years,
                                        level_id,       --11/18/2015
                                        date_created,
                                        created_by,
                                        version)
         (  
         SELECT AWARDGEN_PARTICIPANT_PK_SQ.NEXTVAL,
                     res.batch_id,      --01/21/2016
                     res.user_id,       --01/21/2016
                     res.days,              --01/21/2016
                     award_amount,    --01/21/2016 v_award_amount
                     TO_DATE(res.anniversaryDate,'MM/DD/YYYY'), --01/21/2016
                     res.years,         --01/21/2016
                     res.level_id,   --11/18/2015--01/21/2016
                     SYSDATE,
                     c_created_by,0 FROM
                     ( SELECT 
                 NVL(p_award_generator_batch_id,v_batch_id) AS batch_id,
                 p_award_generator_id awardgen_id,      --01/21/2016
                 pe.user_id,
                 TRUNC (SYSDATE) - TRUNC (pe.hire_date) AS days,                
                    --                    TO_CHAR (TRUNC (pe.hire_date), 'MM/DD')                           --02/04/2019
                    --                 || '/'
                    --                 || CASE
                    --                       WHEN TO_CHAR (p_batch_start_date, 'YYYY') =
                    --                               TO_CHAR (p_batch_end_date, 'YYYY')
                    --                       THEN
                    --                          TO_CHAR (TRUNC (p_batch_start_date), 'YYYY')
                    --                       WHEN     TO_CHAR (p_batch_start_date, 'YYYY') !=
                    --                                   TO_CHAR (p_batch_end_date, 'YYYY')
                    --                            AND TO_CHAR (pe.hire_date, 'MMDD') BETWEEN TO_CHAR (
                    --                                                                          p_batch_start_date,
                    --                                                                          'MMDD')
                    --                                                                   AND '1232'
                    --                       THEN
                    --                          TO_CHAR (TRUNC (p_batch_start_date), 'YYYY')
                    --                       WHEN     TO_CHAR (p_batch_start_date, 'YYYY') !=
                    --                                   TO_CHAR (p_batch_end_date, 'YYYY')
                    --                            AND TO_CHAR (pe.hire_date, 'MMDD') BETWEEN '0100'
                    --                                                                   AND TO_CHAR (
                    --                                                                          p_batch_end_date,
                    --                                                                          'MMDD')
                    --                       THEN
                    --                          TO_CHAR (TRUNC (p_batch_end_date), 'YYYY')
                    --                    END
                    --                    AS anniversaryDate,
                        CASE WHEN MOD(TO_NUMBER(TO_CHAR(p_batch_start_date,'YYYY')),4)=0 AND        --02/04/2019
                                  MOD(TO_NUMBER(TO_CHAR(p_batch_start_date,'YYYY')),100)=0 AND 
                                  MOD(TO_NUMBER(TO_CHAR(p_batch_start_date,'YYYY')),400)=0 AND
                                  TO_CHAR (TRUNC (pe.hire_date), 'MMDD')='0229'                                  
                                  THEN '02/29/'||TO_CHAR(p_batch_start_date,'YYYY')
                             WHEN MOD(TO_NUMBER(TO_CHAR(p_batch_start_date,'YYYY')),4)=0 AND 
                                  MOD(TO_NUMBER(TO_CHAR(p_batch_start_date,'YYYY')),100)>0 AND
                                  TO_CHAR (TRUNC (pe.hire_date), 'MMDD')='0229'                                  
                                  THEN '02/29/'||TO_CHAR(p_batch_start_date,'YYYY')
                             WHEN TO_CHAR (TRUNC (pe.hire_date), 'MMDD')='0229' AND
                                  MOD(TO_NUMBER(TO_CHAR(p_batch_start_date,'YYYY')),4)>0                                                                 
                                  THEN '02/28/'||TO_CHAR(p_batch_start_date,'YYYY')
                             ELSE 
                                 TO_CHAR (TRUNC (pe.hire_date), 'MM/DD')
                                 || '/'
                                 || CASE
                                       WHEN TO_CHAR (p_batch_start_date, 'YYYY') = TO_CHAR (p_batch_end_date, 'YYYY')
                                       THEN
                                            TO_CHAR (TRUNC (p_batch_start_date), 'YYYY')
                                       WHEN TO_CHAR (p_batch_start_date, 'YYYY') != TO_CHAR (p_batch_end_date, 'YYYY')
                                                AND TO_CHAR (pe.hire_date, 'MMDD') BETWEEN TO_CHAR (p_batch_start_date,'MMDD') AND '1232'
                                       THEN
                                            TO_CHAR (TRUNC (p_batch_start_date), 'YYYY')
                                       WHEN TO_CHAR (p_batch_start_date, 'YYYY') != TO_CHAR (p_batch_end_date, 'YYYY')
                                            AND TO_CHAR (pe.hire_date, 'MMDD') BETWEEN '0100' AND TO_CHAR ( p_batch_end_date,'MMDD')
                                       THEN
                                          TO_CHAR (TRUNC (p_batch_end_date), 'YYYY')
                                    END
                          END
                    AS anniversaryDate,
                 CASE
                    WHEN TO_CHAR (p_batch_start_date, 'YYYY') =
                            TO_CHAR (p_batch_end_date, 'YYYY')
                    THEN
                       (  TO_CHAR (TRUNC (p_batch_end_date), 'YYYY')
                        - TO_CHAR (TRUNC (pe.hire_date), 'YYYY'))
                    WHEN     TO_CHAR (p_batch_start_date, 'YYYY') !=
                                TO_CHAR (p_batch_end_date, 'YYYY')
                         AND TO_CHAR (pe.hire_date, 'MMDD') BETWEEN TO_CHAR (
                                                                       p_batch_start_date,
                                                                       'MMDD')
                                                                AND '1232'
                    THEN
                        (  TO_CHAR (TRUNC (p_batch_end_date), 'YYYY')            --02/04/2019
                        - TO_CHAR (TRUNC (pe.hire_date), 'YYYY'))
                    WHEN     TO_CHAR (p_batch_start_date, 'YYYY') !=
                                TO_CHAR (p_batch_end_date, 'YYYY')
                         AND TO_CHAR (pe.hire_date, 'MMDD') BETWEEN '0100'
                                                                AND TO_CHAR (
                                                                       p_batch_end_date,
                                                                       'MMDD')
                    THEN
                       (  TO_CHAR (TRUNC (p_batch_end_date), 'YYYY')
                        - TO_CHAR (TRUNC (pe.hire_date), 'YYYY'))
                 END
                    AS years,
                    aw.level_id     --11/18/2015
            FROM vw_curr_pax_employer pe, participant p,
                  ( SELECT ua.user_id, aw.level_id      --11/18/2015
                      FROM awardgen_award aw,            
                           promo_merch_program_level pm, 
                           promo_merch_country pc,       
                           user_address ua              
                     WHERE aw.awardgen_id = p_award_generator_id              
                       AND aw.level_id = pm.promo_merch_program_level_id           
                       AND pm.promo_merch_country_id = pc.promo_merch_country_id  
                       AND pc.country_id = ua.country_id                           
                       AND ua.is_primary  = 1 ) aw
           WHERE     p.status = 'active'
                 AND p.user_id = pe.user_id
                 AND p.user_id = aw.user_id(+)                                  --11/18/2015
                 AND (   (    TO_CHAR (p_batch_start_date, 'YYYY') =
                                 TO_CHAR (p_batch_end_date, 'YYYY')
                          AND TO_CHAR (hire_date, 'MMDD') BETWEEN TO_CHAR (
                                                                      p_batch_start_date,
                                                                      'MMDD')
                                                               AND TO_CHAR (
                                                                      p_batch_end_date,
                                                                      'MMDD'))
                      OR (    TO_CHAR (p_batch_start_date, 'YYYY') !=
                                 TO_CHAR (p_batch_end_date, 'YYYY')
                          AND (   TO_CHAR (hire_date, 'MMDD') BETWEEN TO_CHAR (
                                                                         p_batch_start_date,
                                                                         'MMDD')
                                                                  AND '1232'
                               OR TO_CHAR (hire_date, 'MMDD') BETWEEN '0100'
                                                                  AND TO_CHAR (
                                                                         p_batch_end_date,
                                                                         'MMDD'))))
                 AND (   EXISTS
                            (SELECT *
                               FROM PROMOTION
                              WHERE     promotion_id = v_promotion_id
                                    AND (CASE
                                           WHEN secondary_audience_type =
                                                   'sameasprimaryaudience'
                                           THEN
                                              primary_audience_type
                                           ELSE
                                              secondary_audience_type
                                        --END = 'allactivepaxaudience' 							--04/12/2019 Bug 79012
                                        END) IN ('allactivepaxaudience','specifyaudience')		--04/12/2019 Bug 79012
                                        )
                      OR EXISTS
                            (SELECT p.promotion_id
                               FROM promo_audience pa,
                                    promotion p,
                                    participant_audience pax
                              WHERE     pax.user_id = pe.user_id
                                    AND pax.audience_id = pa.audience_id
                                    AND pa.promotion_id = p.promotion_id
                                    AND pa.promo_audience_type = 'SECONDARY'
                                    AND p.promotion_id = v_promotion_id)))res,
           awardgen_award awa                                    --01/21/2016
     WHERE res.awardgen_id = awa.awardgen_id --(+)                 --01/21/2016  removed(+) --05/10/2016 Bug#66740
       AND res.years = awa.years           --(+);                --01/21/2016 removed(+)  --05/10/2016 Bug#66740
       and NVL(res.level_id,0)=NVL(awa.LEVEL_ID,0));							--04/12/2019 Bug 79012--05/09/2019 During points promotion level id is returning Null
   ELSE
      --Characteristic date
      SELECT characteristic_id INTO v_characteristic_id FROM characteristic WHERE description = v_examiner_field AND is_active =1;
      
      INSERT INTO AWARDGEN_PARTICIPANT (awardgen_participant_id,
                                        awardgen_batch_id,
                                        user_id,
                                        anniversary_num_days,
                                        award_amount,
                                        anniversary_date,
                                        anniversary_num_years,
                                        level_id,   --11/18/2015
                                        date_created,
                                        created_by,
                                        version)
         ( SELECT AWARDGEN_PARTICIPANT_PK_SQ.NEXTVAL,
                        res.batch_id,       --01/21/2016
                        res.user_id,        --01/21/2016
                        res.days,           --01/21/2016
                        award_amount,       --01/21/2016v_award_amount
                        TO_DATE(res.anniversaryDate,'MM/DD/YYYY'),  --01/21/2016
                        res.years,          --01/21/2016
                        res.level_id,   --11/18/2015--01/21/2016
                        SYSDATE,
                        c_created_by,
                        0 FROM (
                 SELECT NVL(p_award_generator_batch_id,v_batch_id) batch_id,
                 p_award_generator_id awardgen_id,      --01/21/2016
                 p.user_id,
                 aw.level_id,   --11/18/2015
                   TRUNC (SYSDATE)
                 - TRUNC (TO_DATE (uc.characteristic_value, 'mm/dd/YYYY'))
                    AS days,                
                    TO_CHAR (
                       TRUNC (
                          TO_DATE (uc.characteristic_value, 'mm/dd/YYYY')),
                       'MM/DD')
                 || '/'
                 || CASE
                       WHEN TO_CHAR (p_batch_start_date, 'YYYY') =
                               TO_CHAR (p_batch_end_date, 'YYYY')
                       THEN
                          TO_CHAR (TRUNC (p_batch_start_date), 'YYYY')
                       WHEN     TO_CHAR (p_batch_start_date, 'YYYY') !=
                                   TO_CHAR (p_batch_end_date, 'YYYY')
                            AND TO_CHAR (
                                   TO_DATE (uc.characteristic_value,
                                            'mm/dd/YYYY'),
                                   'MMDD') BETWEEN TO_CHAR (
                                                      p_batch_start_date,
                                                      'MMDD')
                                               AND '1232'
                       THEN
                          TO_CHAR (TRUNC (p_batch_start_date), 'YYYY')
                       WHEN     TO_CHAR (p_batch_start_date, 'YYYY') !=
                                   TO_CHAR (p_batch_end_date, 'YYYY')
                            AND TO_CHAR (
                                   TO_DATE (uc.characteristic_value,
                                            'mm/dd/YYYY'),
                                   'MMDD') BETWEEN '0100'
                                               AND TO_CHAR (p_batch_end_date,
                                                            'MMDD')
                       THEN
                          TO_CHAR (TRUNC (p_batch_end_date), 'YYYY')
                    END
                    AS anniversaryDate,
                 CASE
                    WHEN TO_CHAR (p_batch_start_date, 'YYYY') =
                            TO_CHAR (p_batch_end_date, 'YYYY')
                    THEN
                       (  TO_CHAR (TRUNC (p_batch_end_date), 'YYYY')
                        - TO_CHAR (
                             TRUNC (
                                TO_DATE (uc.characteristic_value,
                                         'mm/dd/YYYY')),
                             'YYYY'))
                    WHEN     TO_CHAR (p_batch_start_date, 'YYYY') !=
                                TO_CHAR (p_batch_end_date, 'YYYY')
                         AND TO_CHAR (
                                TO_DATE (uc.characteristic_value,
                                         'mm/dd/YYYY'),
                                'MMDD') BETWEEN TO_CHAR (p_batch_start_date,
                                                         'MMDD')
                                            AND '1232'
                    THEN
                       (  TO_CHAR (TRUNC (p_batch_start_date), 'YYYY')
                        - TO_CHAR (
                             TRUNC (
                                TO_DATE (uc.characteristic_value,
                                         'mm/dd/YYYY')),
                             'YYYY'))
                    WHEN     TO_CHAR (p_batch_start_date, 'YYYY') !=
                                TO_CHAR (p_batch_end_date, 'YYYY')
                         AND TO_CHAR (
                                TO_DATE (uc.characteristic_value,
                                         'mm/dd/YYYY'),
                                'MMDD') BETWEEN '0100'
                                            AND TO_CHAR (p_batch_end_date,
                                                         'MMDD')
                    THEN
                       (  TO_CHAR (TRUNC (p_batch_end_date), 'YYYY')
                        - TO_CHAR (
                             TRUNC (
                                TO_DATE (uc.characteristic_value,
                                         'mm/dd/YYYY')),
                             'YYYY'))
                 END
                    AS years
            FROM user_characteristic uc, participant p,
                 ( SELECT ua.user_id, aw.level_id      --11/18/2015
                      FROM awardgen_award aw,            
                           promo_merch_program_level pm, 
                           promo_merch_country pc,       
                           user_address ua              
                     WHERE aw.awardgen_id = p_award_generator_id              
                       AND aw.level_id = pm.promo_merch_program_level_id           
                       AND pm.promo_merch_country_id = pc.promo_merch_country_id  
                       AND pc.country_id = ua.country_id                           
                       AND ua.is_primary  = 1 ) aw
           WHERE     p.status = 'active'
                 AND p.user_id = uc.user_id 
                 AND p.user_id = aw.user_id(+)                                  --11/18/2015               
                 AND uc.characteristic_id = v_characteristic_id                 
                 AND (   (    TO_CHAR (p_batch_start_date, 'YYYY') =
                                 TO_CHAR (p_batch_end_date, 'YYYY')
                          AND TO_CHAR (
                                 TO_DATE (uc.characteristic_value,
                                          'mm/dd/YYYY'),
                                 'MM/DD') BETWEEN TO_CHAR (
                                                     p_batch_start_date,
                                                     'MM/DD')
                                              AND TO_CHAR (p_batch_end_date,
                                                           'MM/DD'))
                      OR (    TO_CHAR (p_batch_start_date, 'YYYY') !=
                                 TO_CHAR (p_batch_end_date, 'YYYY')
                          AND (   TO_CHAR (
                                     TO_DATE (uc.characteristic_value,
                                              'mm/dd/YYYY'),
                                     'MMDD') BETWEEN TO_CHAR (
                                                        p_batch_start_date,
                                                        'MMDD')
                                                 AND '1232'
                               OR TO_CHAR (
                                     TO_DATE (uc.characteristic_value,
                                              'mm/dd/YYYY'),
                                     'MMDD') BETWEEN '0100'
                                                 AND TO_CHAR (
                                                        p_batch_end_date,
                                                        'MMDD'))))
                 AND (   EXISTS
                            (SELECT *
                               FROM PROMOTION
                              WHERE     promotion_id = v_promotion_id
                                    AND (CASE
                                           WHEN secondary_audience_type =
                                                   'sameasprimaryaudience'
                                           THEN
                                              primary_audience_type
                                           ELSE
                                              secondary_audience_type
                                        --END = 'allactivepaxaudience')							--05/22/2019 Bug 79012 commented
                                        END) IN ('allactivepaxaudience','specifyaudience'))		--05/22/2019 Bug 79012
                      OR EXISTS
                            (SELECT p.promotion_id
                               FROM promo_audience pa,
                                    promotion p,
                                    participant_audience pax
                              WHERE     pax.user_id = uc.user_id
                                    AND pax.audience_id = pa.audience_id
                                    AND pa.promotion_id = p.promotion_id
                                    AND pa.promo_audience_type = 'SECONDARY'
                                    AND p.promotion_id = v_promotion_id))) res,
           awardgen_award awa                                    --01/21/2016
     WHERE res.awardgen_id = awa.awardgen_id --(+)               --01/21/2016 --removed(+) --05/10/2016 Bug#66740
       AND res.years = awa.years             --(+));             --01/21/2016 --removed(+) --05/10/2016 Bug#66740
       and NVL(res.level_id,0)=NVL(awa.LEVEL_ID,0));			 --05/22/2019 Bug 79012
   END IF;

   UPDATE awardgen_batch
      SET use_issue_date = p_use_issue_date, issue_date = p_issue_date
    WHERE awardgen_batch_id = p_award_generator_batch_id;

   UPDATE awardgen_participant
      SET issue_date = NVL (p_issue_date, anniversary_date)
    WHERE awardgen_batch_id = NVL(p_award_generator_batch_id,v_batch_id);

   INSERT INTO awardgen_manager (awardgen_manager_id,
                                 AWARDGEN_BATCH_ID,
                                 user_id,
                                 awardgen_participant_id,
                                 expiry_date,
                                 date_created,
                                 created_by,
                                 version)
      SELECT AWARDGEN_MANAGER_PK_SQ.NEXTVAL,
                  awardgen_batch_id,
                  CASE WHEN manager_user_id = 0 THEN user_id ELSE manager_user_id END,
                  awardgen_participant_id,
                  v_expiry_date,
                  SYSDATE,
                  c_created_by,
                  0
      FROM (SELECT       
             ap.awardgen_batch_id,
             fnc_get_manager_id (ap.user_id, un.node_id) AS manager_user_id,
             ap.user_id,
             ap.awardgen_participant_id             
        FROM awardgen_participant ap, user_node un
       WHERE     ap.awardgen_batch_id = NVL(p_award_generator_batch_id,v_batch_id)
             AND ap.user_id = un.user_id
             AND un.is_primary = 1
             AND un.status = 1);
             
             p_award_generator_batch_id := NVL(p_award_generator_batch_id,v_batch_id);
             p_out_return_code :=0;
             p_out_notify_manager := v_notify_manager;

EXCEPTION WHEN OTHERS THEN
p_out_return_code:=99;
prc_execution_log_entry ('PRC_AWARD_GENERATOR',1,
                            'ERROR',
                            'stage : '||v_stage||' '||SQLERRM,
                            NULL);

END;
/