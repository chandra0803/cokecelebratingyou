CREATE OR REPLACE PROCEDURE prc_sa_datamigration
IS 
   /*********************************************************************************
   -- Purpose:
   -- This procedure is to populate the PURL and pure celebration data to stage tables. 
   --             Later these table's data will be moved to new service awards databases.
   -- Person           Date        Comments
   -- ---------       ----------   -----------------------------------------------------
   -- Gorantla        03/04/2019   Initial Version
   *********************************************************************************/
      
   c_process_name                CONSTANT execution_log.process_name%TYPE  := 'PRC_SA_DATAMIGRATION' ;
   c_release_level               CONSTANT execution_log.release_level%TYPE := 1 ;
   v_stage                                execution_log.text_line%TYPE;
BEGIN

prc_execution_log_entry (c_process_name,c_release_level,'INFO','Procedure execution started' ,NULL);

v_stage := 'DELETE data from stage tables';
FOR rec IN (SELECT table_name
              FROM user_tables
             WHERE table_name IN ('PROGRAM','PROGRAM_AWARD_LEVEL','SA_CELEBRATION_INFO','SA_INVITEANDCONTRIBUTE_INFO') 
                OR table_name like 'STG_%'
             ORDER BY 1 DESC)
LOOP           
  EXECUTE IMMEDIATE 'DELETE FROM '||rec.table_name;  
END LOOP;  

v_stage := 'Populate data into stg_sa_programs1';
INSERT INTO stg_sa_programs1
SELECT stg_sa_programs_id_seq.nextval id,
       (select company_id from company where rownum = 1) companyid, 
       p.promotion_id programid, --EXTRA COLUMN to know DM promotion_id 
       is_include_purl,  -- EXTRA COLUMN to know purl included or not
       fnc_new_uuid program_uuid,
       json_object('en-US' value p.promotion_name,
                   'key' value 'program_PROMOTION_ID.name') programname,
       promotion_start_date programstartdate,
       promotion_end_date programenddate,
       DECODE(promotion_status,'live','Active',promotion_status) programstate,
       DECODE(award_type,'merchandise','Gift Code') awardtype,
       bills_active isbillprofileactive,
       CASE WHEN bills_active = 1 THEN (SELECT json_objectagg('billProfile'||decode(sort_order,0,'01',1,'02',2,'03',3,'04',4,'05',5,'06',6,'07',7,'08',8,'09',9,10) 
                            value json_object('code' value DECODE(bill_code,'userName','loginId','orgUnitName','orgUnit',bill_code),'value' value custom_value)) 
          FROM promo_bill_code WHERE promotion_id = p.promotion_id) 
          ELSE null END billprofiles,
       lower(fnc_new_uuid) giftcodeawardid,
       'Blank' awardintervaltype,  --in SA Replaced with blank ''
       null awardintervalfield,       
       null awardintervalnumber,      
       null awardintervalpart,
       json_object('contentId' value lower(fnc_new_uuid), 'templateId' value CASE WHEN fnc_get_instance = 'qa' THEN '390d4ca7-be28-45ab-83af-9a66f18bad12'
                                                                                  WHEN fnc_get_instance = 'preprod' THEN 'db648a9f-a6a7-45b6-84de-55e759938e3f' 
                                                                             ELSE 'ae2d9340-8fa1-4eee-8d3f-75c937965467' END) recipientemail, 
       json_object('contentId' value lower(fnc_new_uuid), 'templateId' value CASE WHEN fnc_get_instance = 'qa' THEN 'edabbdd4-9dd5-49a3-96c7-c951104fa2d5'
                                                                                  WHEN fnc_get_instance = 'preprod' THEN '8bca2cb1-76d7-4ac7-96c1-f98b0344bd90' 
                                                                             ELSE '704282c1-a7d9-4f69-91d9-b76a7b71f234' END) giftcodereminderemail,  
      (SELECT number_of_days FROM promo_notification WHERE notification_type = 'non_redemption_reminder' AND promotion_id = p.promotion_id) gcrememailsenddaysafter,
      json_object('contentId' value lower(fnc_new_uuid), 'templateId' value CASE WHEN fnc_get_instance = 'qa' THEN 'edabbdd4-9dd5-49a3-96c7-c951104fa2d5'
                                                                                 WHEN fnc_get_instance = 'preprod' THEN '8bca2cb1-76d7-4ac7-96c1-f98b0344bd90' 
                                                                             ELSE '704282c1-a7d9-4f69-91d9-b76a7b71f234' END) giftcodeexpirationemail,            
       (SELECT number_of_days FROM promo_notification WHERE notification_type = 'non_redemption_reminder' AND promotion_id = p.promotion_id) gcexpemailsenddaysprior,
       --start contributionemail
       json_object('reminderEmailSendDaysAfter' value (SELECT number_of_days FROM promo_notification WHERE notification_type = 'non_redemption_reminder' AND promotion_id = p.promotion_id),
                   'contributionEmailSendDaysPrior' value 10 , --default YET 
                   'reminderEmailSendDaysBefore' value (SELECT number_of_days FROM promo_notification WHERE notification_type = 'purl_contributor_nonresponse' AND promotion_id = p.promotion_id),
                   'reminderEmail' value json_object('contentId' value lower(fnc_new_uuid),'templateId' value '49a5c790-404b-4843-a4d7-0afd20c96b29'),
                   'contributionEmail' value json_object('contentId' value lower(fnc_new_uuid),'templateId' value  '5a9c6444-497f-4dae-91e8-3e352fcd177d'),
                   'commentReplyEmail' value json_object('contentId' value lower(fnc_new_uuid),
                                        'templateId' value CASE WHEN fnc_get_instance = 'qa' THEN 'e093505a-7057-43ce-ac63-0b8e39bb2e85'
                                                                WHEN fnc_get_instance = 'preprod' THEN '0f97ecda-2095-4721-ad33-2b7dcdc3f6e8' 
                                                           ELSE '98fbe414-73dc-4685-97eb-c79c396b6ab1' END),
                   'contributorThankYouEmail' value json_object('contentId' value lower(fnc_new_uuid),
                                        'templateId' value CASE WHEN fnc_get_instance = 'qa' THEN 'eab3f7f9-8d62-4928-b611-00560272202a'
                                                                WHEN fnc_get_instance = 'preprod' THEN '9e007997-76b0-4042-b63a-f23869e5a964' 
                                                           ELSE 'b76cad83-c956-4576-b894-27ec6130b4db' END)) contributionemail, 
    --end contributionemail  
       p.date_created createdat,
       NVL(p.date_modified,p.date_created) updatedat,
     -- start  programsetup
      json_object('basics' value 'true' FORMAT JSON,
                  'awards' value decode(pr.award_active,1,'true','false') FORMAT JSON,
                  'emails' value 'true' FORMAT JSON,
                  'milestones' value CASE WHEN (SELECT count(*) FROM promo_merch_program_level pmp,promo_merch_country  pmc where pmp.promo_merch_country_id =pmc.promo_merch_country_id AND pmc.promotion_id = p.promotion_id) > 0 THEN 'true' ELSE 'false' END FORMAT JSON,
                  'celebrations' value 'true' FORMAT JSON) programsetup,
     -- end  programsetup
     (SELECT decode(frequency_type,'daily','Days','weekly','Days','monthly','Monthly','None') FROM promo_notification WHERE notification_type = 'non_redemption_reminder' AND promotion_id = p.promotion_id) gcrememailfrequency,
     null gcrememailsendtime,
     null gcrememailsendstartdate,
     null gcrememailsendenddate,
       --start celebrationsettings
        json_object(
                  'isCelebrationsActive' value 'true' FORMAT JSON,
                    'isShareActive' value 'false' FORMAT JSON,
                    'isYearThatWas' value DECODE(year_tile_enabled,1,'true','false') FORMAT JSON,
	                'isContributionsActive' value DECODE(is_include_purl,1,'true','false') FORMAT JSON,
	                'isCompanyMessageActive' value CASE WHEN default_message IS NULL THEN 'false' ELSE 'true' END FORMAT JSON, 
	                'isCountDownActive' value 'true' FORMAT JSON,
	                'isAwardsDisplayActive' value DECODE(award_active,1,'true','false') FORMAT JSON,
	                'isCompanyTimelineActive' value 'false' FORMAT JSON,
	                'companyMessage' value CASE WHEN default_message IS NULL THEN NULL ELSE json_object('imageUrl' value Default_Celebration_Avatar,
	                                                   'message' value json_object('en-US' value fnc_cms_asset_code_value(default_message),  
	                                                                                 'key' value 'program_PROMOTION_ID.company.message'), -- PROMOTION_ID has to replace
                                                       'videoUrl' value NULL, 
                                                       'name' value Default_Celebration_Name,
                                                       'title' value null) END,
                  'welcomeMessage' value json_object('headerMessage' value json_object('en-US' value 'Congratulations!',
	                                                                                    'key' value 'program_PROMOTION_ID.welcome.header'), -- PROMOTION_ID has to replace
	                                                       'message' value json_object('en-US' value 'This celebration is all about you! Take a moment to browse through the accolades contributed by your co-workers, family or friends as they celebrate your special achievement.', 
	                                                                                   'key' value 'program_PROMOTION_ID.welcome.message')),  -- PROMOTION_ID has to replace
                 'contributionSettings' value json_object('commentsChannelId' value null,
                                                           'isManagerMessageActive' value 'true' FORMAT JSON,  --YET
                                                           'highFivesChannelId' value null, 
                                                           'groupDoodleChannelId' value null,
                                                           'commentSettings' value '{}', 
                                                           'isHighFivesActive' value 'true' FORMAT JSON,
                                                           'isCommentsActive' value 'true' FORMAT JSON,
                                                           'invites' value 'others', -- YET
                                                           'isGroupDoodleActive' value 'true' FORMAT JSON) 	                                                                                                                                      	                                                                                    
                 ) celebrationsettings,
     --end celebrationsettings 
     json_object('multiLevelManagerEmail' value json_object('contentId' value lower(fnc_new_uuid), 'templateId' value '55fcf1a3-f100-4000-b8c2-0d14429672af'),
                 'singleLevelManagerEmail' value json_object('contentId' value lower(fnc_new_uuid), 'templateId' value '9d6a526a-804f-43d2-a9bd-52b9e9974416'),
                 'managerEmailSendDaysPrior' value 10) manageremail, --YET
     null branding,  -- YET
     (select hierarchy_uuid from hierarchy where is_active = 1 and is_primary = 1 and is_deleted =0) hierarchyid,  -- YET
     null emaildisplayname, -- from email name
     null                             
 FROM promotion p,
      promo_recognition pr
WHERE p.promotion_id = pr.promotion_id
 AND p.promotion_status IN ('live','expired')
   AND p.award_type = 'merchandise'
   AND pr.allow_public_recognition = 1
   AND (pr.include_celebrations = 1 OR pr.is_include_purl = 1);
   
v_stage := 'Update stg_sa_programs1 to replace hard coded string with program uuid';   
UPDATE stg_sa_programs1
   SET celebrationsettings = replace(celebrationsettings,'PROMOTION_ID',program_uuid),
       programname = replace(programname,'PROMOTION_ID',program_uuid);
   
v_stage := 'Populate data into stg_gc_countries';   
INSERT INTO stg_gc_countries
SELECT companyid,
       giftcodeawardid awardid,
       c.awardbanq_country_abbrev countrycode,
       pmc.program_id programnumber,
       pmc.date_created createdat,
       NVL(pmc.date_modified,pmc.date_created) updatedat
  FROM stg_sa_programs1 sp,
       promo_merch_country pmc,
       country c
 WHERE sp.programid = pmc.promotion_id
   AND pmc.country_id = c.country_id;    
   
v_stage := 'Populate data into stg_gc_awards';    
INSERT INTO stg_gc_awards
SELECT companyid,
       giftcodeawardid,
       program_uuid,
       createdat,
       updatedat,
       null browsecode,
       billprofiles
  FROM stg_sa_programs1;   
  
v_stage := 'Populate data into stg_gc_awards';     
INSERT INTO stg_sa_milestones
SELECT milestones_id_seq.nextval,
       p.companyid,
       program_uuid,
       fnc_new_uuid() milestoneid, 
       pmpl.om_level_name milestonecode,  
       json_object('en-US' value fnc_cms_asset_code_value(pmpl.cm_asset_key),
                   'key' value 'program_'||program_uuid||'.level_'||pmpl.om_level_name||'_USA.label') milestonename,
       '{}' milestoneaward,
       'Active' milestonestate,
       null ecard,
       'Blank' celebrationtype,  -- in SA Replaced with blank('')
       'Blank' contributiontype, -- in SA Replaced with blank('')
       pmpl.date_created createdat,
       NVL(pmpl.date_modified,pmpl.date_created) updatedat,
       0 maxaward,
       json_object('en-US' value fnc_cms_asset_code_value(pmpl.cm_asset_key),
                   'key' value 'program_'||program_uuid||'.level_'||pmpl.om_level_name||'_USA.description') messagelevellabel,
       DECODE(pr.include_celebrations,0,null,pr.flash_name) webmessageimageurl, 
       DECODE(pr.include_celebrations,0,null,pr.flash_name) emailmessageimageurl, 
       c.awardbanq_country_abbrev
  FROM stg_sa_programs1 p,
       (SELECT *
          FROM promo_recognition pr,
               ecard c
         WHERE (pr.celebration_generic_ecard IS NOT NULL AND c.flash_name like '%' || pr.celebration_generic_ecard || '%')) pr,
       country c,
       promo_merch_country pmc,
       promo_merch_program_level pmpl
 WHERE p.programid = pr.promotion_id(+)
   AND p.programid = pmc.promotion_id
   AND c.country_id = pmc.country_id
   AND pmc.promo_merch_country_id = pmpl.promo_merch_country_id;  
   
v_stage := 'Populate data into stg_gc_levels';     
INSERT INTO stg_gc_levels
SELECT fnc_new_uuid() id,
       p.companyid,
       giftcodeawardid,
       milestonecode levelcode, 
       milestonename levelname,
       null defaultitem,
       m.createdat,
       m.updatedat
  FROM stg_sa_milestones m,
       stg_sa_programs1 p
 WHERE m.programid = p.program_uuid;   
 
v_stage := 'Populate data into stg_sa_persons1'; 
INSERT INTO stg_sa_persons1
SELECT *
  FROM (
WITH pax_info AS   
 -- Get purl recipients
(SELECT user_id
  FROM purl_recipient pr,
       stg_sa_programs1 rp
 WHERE pr.promotion_id = rp.programid
   AND rp.is_include_purl = 1
   AND state != 'archived'
 UNION 
 -- Get purl contibutors
 SELECT pc.user_id
   FROM purl_contributor pc,
        purl_recipient pr
  WHERE pr.purl_recipient_id = pc.purl_recipient_id
    AND pc.user_id IS NOT NULL
    AND pc.purl_recipient_id IN (SELECT purl_recipient_id
                                FROM purl_recipient pr,
                                     stg_sa_programs1 rp
                               WHERE pr.promotion_id = rp.programid
                                 AND rp.is_include_purl = 1
                                 AND state != 'archived')
 UNION
 -- Get manager messages
SELECT manager_id user_id
  FROM (SELECT *
         FROM celebration_manager_message
        WHERE manager_message IS NOT NULL) cmm,
       stg_sa_programs1 ssp
 WHERE cmm.promotion_id = ssp.programid
   AND manager_message IS NOT NULL     
UNION 
-- Get submitter comments
  SELECT c.submitter_id user_id
  FROM claim_recipient cr,
       claim_item ci,
       claim c,
       stg_sa_programs1 rp
 WHERE cr.claim_item_id = ci.claim_item_id
   AND ci.claim_id = c.claim_id
   AND c.promotion_id = rp.programid
   AND rp.is_include_purl = 0                              
 UNION 
 -- Get pure celebration recipients
 SELECT cr.participant_id user_id
  FROM claim_recipient cr,
       claim_item ci,
       claim c,
       stg_sa_programs1 rp
 WHERE cr.claim_item_id = ci.claim_item_id
   AND ci.claim_id = c.claim_id
   AND c.promotion_id = rp.programid
   AND rp.is_include_purl = 0
UNION
-- Get schedule records comments
SELECT sr.recipient_id user_id
  FROM scheduled_recognition sr,
       stg_sa_programs1 rp
 WHERE sr.promotion_id = rp.programid
   AND sr.is_fired = 0)
   SELECT fnc_new_uuid id,
         null purl_contributor_id, -- Extra column 
          pi.user_id,   -- Extra column 
          au.user_name username,
          au.first_name firstname,
          au.last_name lastname,
          uea.email_addr emailaddress,
          au.language_id localecode,
          c.awardbanq_country_abbrev countryiso3code,
          (select company_id from company WHERE rownum = 1) companyid,
          pe.department_type departmentname,
          n.name orgunitname,
          NVL((select json_objectagg((select fnc_cms_asset_code_value(cm_asset_code) 
                                    from characteristic 
                                   where characteristic_id = uc.characteristic_id
                                     ) VALUE uc.characteristic_value)
             from user_characteristic uc,
                  characteristic c
            where c.characteristic_id = uc.characteristic_id
            and is_active = 1
            and user_id = au.user_id),'{}') characteristics,
           au.date_created createdat,
           NVL(au.date_modified,au.date_created) updatedat,
           au.rosterpersonid rosterpersonid,
           p.avatar_original avatarurl,
           sysdate acceptedtcat,
           'true' canauthenticate
     FROM pax_info pi,
          application_user au,
          participant p,
          user_email_address uea,
          user_address ua,
          vw_curr_pax_employer pe,
          user_node un,
          node n,
          country c
    WHERE pi.user_id = au.user_id
      AND au.user_id = p.user_id
      AND au.user_id = uea.user_id
      AND uea.user_id = ua.user_id
      AND ua.user_id = pe.user_id
      AND ua.user_id = un.user_id
      AND un.node_id = n.node_id
      AND ua.country_id = c.country_id
      AND au.user_type = 'pax'
      AND uea.is_primary = 1
      AND ua.is_primary = 1
      AND un.is_primary = 1
  UNION ALL
  -- External users
  SELECT fnc_new_uuid id,
          pc.purl_contributor_id, -- Extra column
          null user_id,   -- Extra column
          null username,
          pc.first_name firstname,
          pc.last_name lastname,
          pc.email_addr emailaddress,
          null localecode,
          null countryiso3code,
          (select company_id from company WHERE rownum = 1) companyid,
          null departmentname,
          null orgunitname,
          '{}' characteristics,
           pc.date_created createdat,
           NVL(pc.date_modified,pc.date_created) updatedat,
           null rosterpersonid,
           pc.avatar_url avatarurl,  
           sysdate acceptedtcat,
           'true' canauthenticate
  FROM stg_sa_programs1 p,
       purl_recipient pr,
       purl_contributor pc
 WHERE p.programid = pr.promotion_id
   AND pr.purl_recipient_id = pc.purl_recipient_id
   AND pc.user_id is null );         
    
v_stage := 'Populate data into stg_sa_persons_type'; 
INSERT INTO stg_sa_persons_type 
SELECT *
  FROM (
WITH pax_info AS   
 -- Get purl recipients
(SELECT null anniversary_num_years,cmm.celebration_manager_message_id,user_id,'purl_celebrator' user_type,pr.award_level_id award_level_id,rp.programid,pr.purl_recipient_id,pr.claim_id claim_id,pr.state,pr.award_date,pr.date_created,pr.date_modified
  FROM purl_recipient pr,
       stg_sa_programs1 rp,
       (SELECT *
         FROM celebration_manager_message
        WHERE manager_message IS NOT NULL) cmm
 WHERE pr.promotion_id = rp.programid
   AND pr.user_id = cmm.recipient_id(+)
   AND pr.promotion_id = cmm.promotion_id(+)
   AND rp.is_include_purl = 1
   AND state != 'archived'  
 UNION ALL
 -- Get purl contibutors
 SELECT null anniversary_num_years, cmm.celebration_manager_message_id,pc.user_id,'purl_contributor' user_type,null award_level_id,pr.promotion_id programid,pr.purl_recipient_id,pr.claim_id claim_id,pr.state,pr.award_date,pc.date_created,pc.date_modified
   FROM purl_contributor pc,
        purl_recipient pr,
       (SELECT *
         FROM celebration_manager_message
        WHERE manager_message IS NOT NULL) cmm
  WHERE pr.purl_recipient_id = pc.purl_recipient_id
    AND pr.user_id = cmm.recipient_id(+)
    AND pr.promotion_id = cmm.promotion_id(+)
    AND pc.user_id IS NOT NULL
    AND pc.purl_recipient_id IN (SELECT purl_recipient_id
                                FROM purl_recipient pr,
                                     stg_sa_programs1 rp
                               WHERE pr.promotion_id = rp.programid
                                 AND rp.is_include_purl = 1
                                 AND state != 'archived')
 UNION ALL
 -- Get manager messages
 SELECT null anniversary_num_years,cmm.celebration_manager_message_id,cmm.manager_id,'manger_contributor' user_type,null award_level_id,cmm.promotion_id programid,null purl_recipient_id,null claim_id,null state,null award_date,cmm.date_created,cmm.date_modified
  FROM (SELECT *
         FROM celebration_manager_message
        WHERE manager_message IS NOT NULL) cmm,
       stg_sa_programs1 p
 WHERE cmm.promotion_id = p.programid   
 UNION ALL
 -- Get submitter comments
 SELECT null anniversary_num_years,rc.celebration_manager_message_id,c.submitter_id,'manger_contributor' user_type,null award_level_id,c.promotion_id programid,null purl_recipient_id,c.claim_id,null state,null award_date,c.date_created,c.date_modified
  FROM stg_sa_programs1 p,
       claim c,
       recognition_claim rc
 WHERE p.programid = c.promotion_id
   AND c.claim_id = rc.claim_id                                    
 UNION ALL
 -- Get pure celebration recipients
SELECT rc.anniversary_num_years anniversary_num_years,cmm.celebration_manager_message_id,cr.participant_id user_id,'claim_celebrator' user_type,mo.promo_merch_program_level_id award_level_id,rp.programid,null purl_recipient_id,c.claim_id,'Notified' state,ci.date_approved award_date,ci.date_created,ci.date_modified
  FROM claim_recipient cr,
       claim_item ci,
       recognition_claim rc,
       claim c,
       merch_order mo,
       stg_sa_programs1 rp,
       (SELECT *
         FROM celebration_manager_message
        WHERE manager_message IS NOT NULL) cmm
 WHERE cr.claim_item_id = ci.claim_item_id
   AND ci.claim_id = rc.claim_id
   AND ci.claim_id = c.claim_id
   AND c.claim_id = mo.claim_id
   AND c.promotion_id = rp.programid
   AND rp.is_include_purl = 0
   and cr.participant_id = cmm.recipient_id(+)
   and c.promotion_id = cmm.promotion_id(+)
UNION ALL
SELECT to_number(sr.anniversary_num_years) anniversary_num_years,to_number(sr.celebration_mgr_message_id) celebration_manager_message_id,sr.recipient_id user_id,'schedule_celebrator' user_type,to_number(sr.level_id) award_level_id,rp.programid,null purl_recipient_id,null claim_id,'Scheduled' state,sr.delivery_date award_date,sr.date_created,sr.date_modified
  FROM scheduled_recognition sr,
       stg_sa_programs1 rp
 WHERE sr.promotion_id = rp.programid
   AND sr.is_fired = 0)
   SELECT sp.id,
          pi.user_id,
          pi.claim_id,
          pi.purl_recipient_id,
          pi.celebration_manager_message_id, 
          pi.state,
          from_tz(cast (pi.award_date as timestamp),'CST') at time zone 'UCT' award_date,
          pmpl.om_level_name,
          pi.programid,
          pi.user_type,
          au.user_name username,
          au.first_name firstname,
          au.last_name lastname,
          uea.email_addr emailaddress,
          au.language_id localecode,
          c.awardbanq_country_abbrev countryiso3code,
          (select company_id from company WHERE rownum = 1) companyid,
          pe.department_type departmentname,
          n.name orgunitname,
          NVL((select json_objectagg((select fnc_cms_asset_code_value(cm_asset_code) 
                                    from characteristic 
                                   where characteristic_id = uc.characteristic_id
                                     ) VALUE uc.characteristic_value)
             from user_characteristic uc,
                  characteristic c
            where c.characteristic_id = uc.characteristic_id
            and is_active = 1
            and user_id = au.user_id),'{}') characteristics,
           pi.date_created createdat,
           NVL(pi.date_modified,pi.date_created) updatedat,
           au.rosterpersonid rosterpersonid,
           p.avatar_original avatarurl,
           sysdate acceptedtcat,
            anniversary_num_years
     FROM pax_info pi,
          promo_merch_program_level pmpl,
          application_user au,
          participant p,
          stg_sa_persons1 sp,
          user_email_address uea,
          user_address ua,
          vw_curr_pax_employer pe,
          user_node un,
          node n,
          country c
    WHERE pi.award_level_id = pmpl.promo_merch_program_level_id(+)
      AND pi.user_id = au.user_id
      AND au.user_id = p.user_id
      AND au.user_id = sp.user_id
      AND au.user_id = uea.user_id
      AND uea.user_id = ua.user_id
      AND ua.user_id = pe.user_id
      AND ua.user_id = un.user_id
      AND un.node_id = n.node_id
      AND ua.country_id = c.country_id
      AND au.user_type = 'pax'
      AND uea.is_primary = 1
      AND ua.is_primary = 1
      AND un.is_primary = 1);
      
v_stage := 'Populate data into stg_gc_giftcodes1';       
INSERT INTO stg_gc_giftcodes1
SELECT p.companyid,
       p.giftcodeawardid awardid,
       fnc_new_uuid giftcodeid,
       fnc_new_uuid requestid,
       c.awardbanq_country_abbrev countrycode,
       m.batch_id batchid,
       pmp.program_id programnumber,
       pmp.om_level_name levelname,
       fnc_java_decrypt(gift_code)||fnc_java_decrypt(gift_code_key) giftcode,
       m.reference_number referencenumber ,       
       NVL(m.expiration_date, add_months(trunc(sysdate), 24)) expiredat,       
       fnc_cms_asset_code_value(pmp.cm_asset_key) description,   
       au.user_id,  -- Extra column  
       m.claim_id,  -- Extra column 
       au.first_name firstname,       
       au.last_name lastname,        
       au.language_id locale,          
       'Service Awards' requestedby,     
       CASE WHEN m.is_redeemed = 1 THEN 'Redeemed'
            WHEN m.is_redeemed = 0 AND m.order_status = 'invalid' THEN 'Cancelled'
            WHEN m.is_redeemed = 0 AND m.order_status = 'expired' THEN 'Expired'
            ELSE 'Issued'
       END status,       
       json_object('billProfile01' VALUE json_object('value' value BILLING_CODE1),
                   'billProfile02' VALUE json_object('value' value BILLING_CODE2),
                   'billProfile03' VALUE json_object('value' value BILLING_CODE3),
                   'billProfile04' VALUE json_object('value' value BILLING_CODE4),
                   'billProfile05' VALUE json_object('value' value BILLING_CODE5),
                   'billProfile06' VALUE json_object('value' value BILLING_CODE6),
                   'billProfile07' VALUE json_object('value' value BILLING_CODE7),
                   'billProfile08' VALUE json_object('value' value BILLING_CODE8),
                   'billProfile09' VALUE json_object('value' value BILLING_CODE9),
                   'billProfile10' VALUE json_object('value' value BILLING_CODE10)) billprofiles,    
       m.date_created createdat,       
       NVL(m.date_modified,m.date_created) updatedat,       
       name orgunitname,     
       sysdate statusupdatedat, 
       sysdate returnedat          
  FROM merch_order m,
       merch_order_bill_code mbc,
       application_user au,
       stg_sa_persons1 sp,
       user_node un,
       node n,
       promo_merch_program_level pmp,
       promo_merch_country pmc,
       country c,
       stg_sa_programs1 p
 WHERE m.participant_id = au.user_id
   AND m.merch_order_id = mbc.merch_order_id(+)
   AND au.user_id = un.user_id
   AND au.user_id = sp.user_id
   AND un.node_id = n.node_id
   AND un.is_primary = 1
   AND m.promo_merch_program_level_id = pmp.promo_merch_program_level_id
   AND pmp.promo_merch_country_id = pmc.promo_merch_country_id
   AND pmc.country_id = c.country_id
   AND pmc.promotion_id = p.programid;  
    
v_stage := 'Populate data into stg_sa_recipients1';      
INSERT INTO stg_sa_recipients1 
SELECT recipients_id_seq.nextval,
       sp.user_type,
       sp.claim_id, -- Extra column 
       sp.user_id, -- Extra column - to identy type of unique records
       sp.purl_recipient_id, -- Extra column
       sp.celebration_manager_message_id,  -- Extra column
       p.companyid,
       fnc_new_uuid recipientid,
       DECODE(au.is_active,1,'Active','Inactive') recipientstate,
       CASE WHEN sp.state = 'expired' THEN 'Cancelled'
            WHEN sp.state IN ('invitation','contribution') THEN 'Scheduled'
            WHEN sp.state = 'recognition' THEN 'Notified'
            WHEN mo.order_status = 'expired' THEN 'Expired' 
            WHEN sp.state = 'complete' AND mo.is_redeemed =0 THEN 'Notified'
            WHEN sp.state = 'complete' AND mo.is_redeemed =1 THEN 'Redeemed'
            ELSE sp.state
        END recipientstatus,
       p.program_uuid programid,
       sp.om_level_name milestonecode,
       sp.award_date awarddate,
       NVL(mo.expiration_date, add_months(trunc(sysdate), 24)) awardexpirationdate,
       gc.giftcodeid,
       mo.reference_number giftcodereference,
       'DayMaker' createdby,
       'DayMaker Migration' createdreference,
       sp.createdat,
       sp.updatedat,
       'America/Chicago' awarddatetimezone,
       fnc_new_uuid celebrationid,
       json_object(
                  'isCelebrationsActive' value 'true' FORMAT JSON,
                    'isShareActive' value 'false' FORMAT JSON,
                    'isYearThatWas' value CASE WHEN pr.service_anniversary = 1 AND pr.anniversary_in_years = 1 AND pr.year_tile_enabled = 1 THEN 'true' ELSE 'false' END FORMAT JSON,
                    'isContributionsActive' value DECODE(p.is_include_purl,1,'true','false') FORMAT JSON,
	                'isCompanyMessageActive' value CASE WHEN default_message IS NULL THEN 'false' ELSE 'true' END FORMAT JSON, 
	                'isCountDownActive' value 'true' FORMAT JSON,
	                'isAwardsDisplayActive' value DECODE(award_active,1,'true','false') FORMAT JSON,
	                'isCompanyTimelineActive' value 'false' FORMAT JSON,
	                'companyMessage' value CASE WHEN default_message IS NULL THEN NULL ELSE json_object('imageUrl' value Default_Celebration_Avatar,
	                                                   'message' value json_object('en-US' value fnc_cms_asset_code_value(default_message),  
	                                                                                 'key' value 'program_PROMOTION_ID.company.message'), -- PROMOTION_ID has to replace
                                                       'videoUrl' value NULL, 
                                                       'name' value Default_Celebration_Name,
                                                       'title' value null) END, 
                  'welcomeMessage' value json_object('headerMessage' value json_object('en-US' value 'Congratulations!', 
	                                                                                    'key' value 'program_PROMOTION_ID.welcome.header'), -- PROMOTION_ID has to replace
	                                                       'message' value json_object('en-US' value 'This celebration is all about you! Take a moment to browse through the accolades contributed by your co-workers, family or friends as they celebrate your special achievement.', 
	                                                                                   'key' value 'program_PROMOTION_ID.welcome.message')),  -- PROMOTION_ID has to replace
                 'contributionSettings' value json_object('commentsChannelId' value fnc_new_uuid,
                                                           'isManagerMessageActive' value 'true' FORMAT JSON,
                                                           'highFivesChannelId' value fnc_new_uuid, 
                                                           'groupDoodleChannelId' value fnc_new_uuid,
                                                           'commentSettings' value '{}', 
                                                           'isHighFivesActive' value 'true' FORMAT JSON,
                                                           'isCommentsActive' value 'true' FORMAT JSON,
                                                           'invites' value 'others',
                                                           'isGroupDoodleActive' value 'true' FORMAT JSON) 	                                                                                                                                      	                                                                                    
                 ) celebrationsettings,
       sp.id participantpersonid,  
       null manager1personid,
       null manager2personid,
       CASE WHEN sp.anniversary_num_years IS NULL THEN extract(year from hire_Date) 
         ELSE extract(year from sysdate)-sp.anniversary_num_years END celebrationyear, -- year that was
       json_object('recipientEmailSentAt' VALUE sp.createdat,
                   'managerEmailSentAt' VALUE sp.createdat,
                   'contributorThankYouEmailSentAt' VALUE sp.createdat,
                   'teamAutoInviteEmailSentAt' VALUE sp.createdat,
                   'reminderEmailSentAt' VALUE sp.createdat,
                   'expirationEmailSentAt' VALUE sp.createdat,
                   'contributionReminderEmailSentAt' VALUE sp.createdat) emailsettings,
       null awardeventsentat
  FROM application_user au,
       vw_curr_pax_employer pe,
       stg_sa_persons_type sp,
       stg_sa_persons1 ssp,
       merch_order mo,
       promo_merch_program_level pmpl,
       stg_gc_giftcodes1 gc,
       stg_sa_programs1 p,
       promo_recognition pr,
       purl_recipient rec
 WHERE au.user_id = pe.user_id
   AND au.user_id = sp.user_id
   AND sp.user_id = ssp.user_id
   AND sp.claim_id = mo.claim_id(+)
   AND mo.claim_id = gc.claim_id(+)
   AND mo.promo_merch_program_level_id = pmpl.promo_merch_program_level_id(+)
   AND sp.programid = p.programid
   AND sp.purl_recipient_id = rec.purl_recipient_id(+)
   AND p.programid = pr.promotion_id
   AND sp.user_type NOT IN ('purl_contributor','manger_contributor');
  
v_stage := 'Update stg_sa_recipients1 to replace hard coded string with program uuid';   
UPDATE stg_sa_recipients1
   SET celebrationsettings = replace(celebrationsettings,'PROMOTION_ID',programid);      
 
v_stage := 'Populate data into stg_sa_celebrationspersons';   
INSERT INTO stg_sa_celebrationspersons 
SELECT celebrationid,
       fnc_new_uuid contributionid,
       personid,
       role,
       sysdate createdat,
       sysdate updatedat,
       'mobilecode' mobilecode,
       sysdate  invitationsentat,
       null inviterpersonid
  FROM 
(SELECT distinct spt.celebrationid,ss.id personid,'peer' role
  FROM stg_sa_persons1 ss,
(SELECT sr.celebrationid,spt.user_id,spt.user_type
  FROM stg_sa_recipients1 sr,
       stg_sa_persons_type spt
 WHERE spt.purl_recipient_id = sr.purl_recipient_id) spt
 WHERE ss.user_id = spt.user_id
  AND spt.user_type = 'purl_contributor'
UNION 
-- External users
SELECT  distinct sr.celebrationid,ss.id personid,'peer' role
  FROM stg_sa_persons1 ss,
       (SELECT pc.*
         FROM purl_recipient pr,
              purl_contributor pc
        WHERE pr.purl_recipient_id = pc.purl_recipient_id
          AND pc.user_id is null) spt,
       stg_sa_recipients1 sr
 WHERE spt.purl_recipient_id = sr.purl_recipient_id
   AND ss.purl_contributor_id = spt.purl_contributor_id
UNION 
SELECT distinct sr.celebrationid,ss.id personid,'peer' role
    FROM stg_sa_persons1 ss,
         stg_sa_persons_type spt,
         stg_sa_recipients1 sr
   WHERE ss.user_id = spt.user_id
    AND spt.celebration_manager_message_id = sr.celebration_manager_message_id
    AND spt.user_type = 'manger_contributor'
UNION
SELECT distinct sr.celebrationid,ss.id personid,'peer' role
    FROM stg_sa_persons1 ss,
         stg_sa_persons_type spt,
         stg_sa_recipients1 sr
   WHERE ss.user_id = spt.user_id
    AND spt.claim_id = sr.claim_id
    AND spt.user_type = 'manger_contributor'
UNION
SELECT celebrationid,participantpersonid personid,'celebrator' role
  FROM stg_sa_recipients1);        
        
v_stage := 'Populate data into stg_chan_channels';  
INSERT INTO stg_chan_channels
SELECT channelId id,
       'false' isDeleted,
       createdat,
       updatedat,
       companyid
  FROM (SELECT regexp_replace(JSON_QUERY(a.celebrationsettings, '$.contributionSettings[*].commentsChannelId' WITH WRAPPER), '\["|\"]') AS commentsChannelId,
               regexp_replace(JSON_QUERY(a.celebrationsettings, '$.contributionSettings[*].highFivesChannelId' WITH WRAPPER), '\["|\"]') AS highFivesChannelId,
               regexp_replace(JSON_QUERY(a.celebrationsettings, '$.contributionSettings[*].groupDoodleChannelId' WITH WRAPPER), '\["|\"]') AS groupDoodleChannelId,
               a.createdat,
               a.updatedat,
               a.companyid
          FROM stg_sa_recipients1 a) t1
UNPIVOT (channelid FOR data IN (commentsChannelId, highFivesChannelId,groupDoodleChannelId)); 
   
v_stage := 'Populate data into stg_chan_messages';  
INSERT INTO stg_chan_messages  
SELECT fnc_new_uuid id, cha.*
  FROM 
(SELECT regexp_replace(JSON_QUERY(sr.celebrationsettings, '$.contributionSettings[*].commentsChannelId' WITH WRAPPER), '\["|\"]') AS channelId,
       first_name||' '||last_name contributorName,
       ssp.id ContributorReference, 
       pcc.comments text,
       null effects,  -- In SA replaced null with blank
       null replyToId,
       fnc_new_uuid threadId,
       'false' isDeleted,
       pcc.date_created createdAt,
       NVL(pcc.date_modified,pcc.date_created) updatedAt, 
       p.companyid,
       'false' isFlagged,
       json_array(CASE WHEN (video_url IS NULL OR mv.request_id IS NULL) THEN NULL 
                     ELSE json_object('id' value fnc_new_uuid,
                                      'url' VALUE mv.mp4_url,
                                      'type' value mv.original_format,
                                      'status' VALUE 'uploaded',
                                      'thumbnailUrl' VALUE mv.thumbnail_image_url) 
                    END,
                  CASE WHEN image_url IS NULL THEN null
                    ELSE json_object('id' value fnc_new_uuid,
                                      'url' VALUE image_url,
                                      'type' value 'image/jpeg')
                  END ) attachments,
        'false' isPrivate,
        'peer' contributorStatus          
  FROM stg_sa_programs1 p,
       purl_recipient pr,
       stg_sa_recipients1 sr,
       purl_contributor pc,
       stg_sa_persons1 ssp,
       (SELECT substr(video_url,instr(video_url,':',-1)+1) request_id,pcc.*
          FROM purl_contributor_comment pcc) pcc,
       mtc_video mv
 WHERE p.programid = pr.promotion_id
   AND pr.purl_recipient_id = sr.purl_recipient_id 
   AND pr.purl_recipient_id = pc.purl_recipient_id
   AND pc.purl_contributor_id = pcc.purl_contributor_id
   AND pc.user_id IS NOT NULL
   AND pc.user_id = ssp.user_id(+)
   AND pcc.request_id = mv.request_id(+)
   AND trunc(mv.expiry_date(+)) > trunc(sysdate) 
   AND sr.user_type = 'purl_celebrator'
UNION ALL
SELECT distinct regexp_replace(JSON_QUERY(sr.celebrationsettings, '$.contributionSettings[*].commentsChannelId' WITH WRAPPER), '\["|\"]') AS channelId,
       first_name||' '||last_name contributorName,
       ss.id ContributorReference,
       pcc.comments text,
       null effects,  -- In SA replaced null with blank
       null replyToId,
       fnc_new_uuid threadId,
       'false' isDeleted,
       pcc.date_created createdAt,
       NVL(pcc.date_modified,pcc.date_created) updatedAt, 
       sr.companyid,
       'false' isFlagged,
       json_array(CASE WHEN (video_url IS NULL OR mv.request_id IS NULL) THEN NULL 
                     ELSE json_object('id' value fnc_new_uuid,
                                      'url' VALUE mv.mp4_url,
                                      'type' value mv.original_format,
                                      'status' VALUE 'uploaded',
                                      'thumbnailUrl' VALUE mv.thumbnail_image_url) 
                    END,
                  CASE WHEN image_url IS NULL THEN null
                    ELSE json_object('id' value fnc_new_uuid,
                                      'url' VALUE image_url,
                                      'type' value 'image/jpeg')
                  END ) attachments,
        'false' isPrivate,
        'peer' contributorStatus 
  FROM stg_sa_persons1 ss,
       (SELECT pc.*
         FROM purl_recipient pr,
              purl_contributor pc
        WHERE pr.purl_recipient_id = pc.purl_recipient_id
          AND pc.user_id is null) spt,
       stg_sa_recipients1 sr,
       (SELECT substr(video_url,instr(video_url,':',-1)+1) request_id,pcc.*
          FROM purl_contributor_comment pcc) pcc,
        mtc_video mv
 WHERE spt.purl_recipient_id = sr.purl_recipient_id
   AND ss.purl_contributor_id = spt.purl_contributor_id
   AND ss.purl_contributor_id = pcc.purl_contributor_id
   AND pcc.request_id = mv.request_id(+)
   AND trunc(mv.expiry_date(+)) > trunc(sysdate)
UNION ALL   
   SELECT regexp_replace(JSON_QUERY(sr.celebrationsettings, '$.contributionSettings[*].commentsChannelId' WITH WRAPPER), '\["|\"]') AS channelId,
       first_name||' '||last_name contributorName,
       ssp.id ContributorReference, 
       manager_message text,
       null effects,  -- in SA replaced null with blank
       null replyToId,
       fnc_new_uuid threadId,
       'false' isDeleted,
       cmm.date_created createdAt,
       NVL(cmm.date_modified,cmm.date_created) updatedAt, 
       sr.companyid,
       'false' isFlagged,
       '[]' attachments,
        'false' isPrivate,
        'manager' contributorStatus  
     FROM stg_sa_persons1 ssp,
          stg_sa_recipients1 sr,
          celebration_manager_message cmm,
          application_user au
    WHERE ssp.user_id = cmm.manager_id
      AND sr.celebration_manager_message_id = cmm.celebration_manager_message_id
      AND cmm.manager_id = au.user_id
UNION ALL
-- Get submitter comments
SELECT regexp_replace(JSON_QUERY(sr.celebrationsettings, '$.contributionSettings[*].commentsChannelId' WITH WRAPPER), '\["|\"]') AS channelId,
       first_name||' '||last_name contributorName,
       ssp.id ContributorReference, 
       rc.submitter_comments text,
       null effects,  --  in SA replaced null with blank
       null replyToId,
       fnc_new_uuid threadId,
       'false' isDeleted,
       c.date_created createdAt,
       NVL(c.date_modified,c.date_created) updatedAt, 
       sr.companyid,
       'false' isFlagged,
       '[]' attachments,
        'false' isPrivate,
        'manager' contributorStatus
  FROM stg_sa_persons1 ssp,
      stg_sa_recipients1 sr,
      recognition_claim rc,
      claim c,
      claim_item ci,
      claim_recipient cr,
      stg_sa_programs1 p,
      application_user au
WHERE ssp.user_id = c.submitter_id
  AND sr.claim_id = rc.claim_id
  AND c.claim_id = ci.claim_id
  AND ci.claim_item_id = cr.claim_item_id
  and (sr.claim_id = c.claim_id and sr.user_id = cr.participant_id)
  AND c.promotion_id = p.programid
  AND rc.submitter_comments is not null
  AND c.submitter_id = au.user_id) cha;
   
v_stage := 'Populate data into stg_cmx_key1';     
INSERT INTO stg_cmx_key1
SELECT fnc_new_uuid id,
       companyMessageKey name,
       companyMessageValue value,  -- Extra column to insurt values into 'string' table
       '54b8cee7-85cf-42f5-aadb-622220cdd07c' projectid,  
       null tags,
       p.createdat,
       p.updatedat,
       'false' donottranslate,
       json_array(json_object('name' value 'programId',
                              'value' value p.program_uuid)) metadata    
  FROM (  
SELECT regexp_replace(JSON_QUERY(p.programname, '$.key' WITH WRAPPER), '\["|\"]') AS companyMessageKey,
       regexp_replace(JSON_QUERY(p.programname, '$."en-US"' WITH WRAPPER), '\["|\"]') AS companyMessageValue,
       program_uuid programid
  FROM stg_sa_programs1 p
UNION
SELECT regexp_replace(JSON_QUERY(p.celebrationsettings, '$.companyMessage[*].message.key' WITH WRAPPER), '\["|\"]') AS companyMessageKey,
       regexp_replace(JSON_QUERY(p.celebrationsettings, '$.companyMessage.message."en-US"' WITH WRAPPER), '\["|\"]') AS companyMessageValue,
       program_uuid programid
  FROM stg_sa_programs1 p
UNION
SELECT regexp_replace(JSON_QUERY(p.celebrationsettings, '$.welcomeMessage[*].headerMessage.key' WITH WRAPPER), '\["|\"]') AS companyMessageKey,
       regexp_replace(JSON_QUERY(p.celebrationsettings, '$.welcomeMessage.headerMessage."en-US"' WITH WRAPPER), '\["|\"]') AS companyMessageValue,
       program_uuid programid
  FROM stg_sa_programs1 p
UNION
SELECT regexp_replace(JSON_QUERY(p.celebrationsettings, '$.welcomeMessage[*].message.key' WITH WRAPPER), '\["|\"]') AS companyMessageKey,
       regexp_replace(JSON_QUERY(p.celebrationsettings, '$.welcomeMessage.message."en-US"' WITH WRAPPER), '\["|\"]') AS companyMessageValue,
       program_uuid programid
  FROM stg_sa_programs1 p
UNION
SELECT distinct regexp_replace(JSON_QUERY(milestonename, '$.key[*]' WITH WRAPPER), '\["|\"]') AS companyMessageKey,
                regexp_replace(JSON_QUERY(milestonename, '$."en-US"' WITH WRAPPER), '\["|\"]') AS companyMessageValue,
                programid
  FROM stg_sa_milestones
UNION
SELECT distinct regexp_replace(JSON_QUERY(messagelevellabel, '$.key[*]' WITH WRAPPER), '\["|\"]') AS companyMessageKey,
                regexp_replace(JSON_QUERY(messagelevellabel, '$."en-US"' WITH WRAPPER), '\["|\"]') AS companyMessageValue,
                programid
  FROM stg_sa_milestones) cm,
   stg_sa_programs1 p
  WHERE companyMessageKey is NOT NULL
    AND cm.programid = p.program_uuid;
    
v_stage := 'Populate data into stg_cmx_string';     
INSERT INTO stg_cmx_string
SELECT id keyid,
       'en-US' locale,
       (select company_id from company where rownum = 1) companyid,
       value,
       createdat,
       updatedat
  FROM stg_cmx_key1;
  
-- Migrate within DM  
v_stage := 'Populate data into program';     
INSERT INTO program
SELECT  promotion_pk_sq.nextval,
         sp.program_uuid,
         (select company_id from company where rownum = 1) companyid, 
         programname,
         p.promotion_name,
         p.promotion_type,
         p.promotion_status,
         p.promotion_start_date,
         p.promotion_end_date,
         p.award_type,
         p.date_created,
         pr.is_include_purl,
         'Congratulations!' program_header,
         'program_'||program_uuid||'.welcome.header' program_header_cmx_asset_code,
         null primary_color,
         null secondary_color,
         p.created_by,
         p.date_created,
         p.modified_by,
         p.date_modified,
         p.version
    FROM promotion p ,
         promo_recognition pr,
         stg_sa_programs1 sp
   WHERE p.promotion_id = pr.promotion_id
     AND pr.promotion_id = sp.programid
     AND p.promotion_status IN ('live','expired')
     AND p.award_type = 'merchandise'
	 AND pr.allow_public_recognition = 1
     AND (pr.is_include_purl = 1 
         OR pr.include_celebrations = 1);
         
v_stage := 'Populate data into program_award_level';          
INSERT INTO program_award_level
SELECT program_award_level_pk_sq.nextval, 
       pg.program_id,
       om_level_name award_level,
       (SELECT awardbanq_country_abbrev FROM country WHERE country_id = pmc.country_id) country,
       fnc_cms_asset_code_value(cm_asset_key) celeb_label,
       'program_'||PROGRAM_UUID||'.'||fnc_cms_asset_code_value(cm_asset_key)||'.label' celeb_label_cmx_asset_code,
       fnc_cms_asset_code_value(cm_asset_key) celeb_msg,
       'program_'||PROGRAM_UUID||'.'||fnc_cms_asset_code_value(cm_asset_key)||'.description' celeb_msg_cmx_asset_code,
       flash_name celeb_img_url,
       fnc_cms_asset_code_value(cm_asset_key) celeb_img_desc,
       'program_'||PROGRAM_UUID||'.'||fnc_cms_asset_code_value(cm_asset_key)||'.description' celeb_img_desc_cmx_asset_code,
       pmpl.created_by,
       pmpl.date_created,
       pmpl.modified_by,
       pmpl.date_modified,
       pmpl.version
     FROM stg_sa_programs1 p,
       program pg,
          promo_recognition pr,
   (SELECT *
     FROM promo_recognition pr,
          ecard c
    WHERE pr.include_celebrations = 1 
      AND pr.celebration_generic_ecard IS NOT NULL 
      AND c.flash_name like '%' || pr.celebration_generic_ecard || '%') ec,
       promo_merch_country pmc,
       promo_merch_program_level pmpl
 WHERE p.program_uuid = pg.external_program_id
   AND p.programid = pr.promotion_id    
   AND p.programid = pr.promotion_id(+)
   AND p.programid = ec.promotion_id(+)
   AND p.programid = pmc.promotion_id
   AND pmc.promo_merch_country_id = pmpl.promo_merch_country_id;        
  
v_stage := 'Populate data into sa_celebration_info';  
INSERT INTO sa_celebration_info
  SELECT sa_celebration_info_pk_sq.nextval,s.*
  FROM 
(SELECT distinct 
         p.program_id,
         sp.recipientid celebrationid,
         sp.claim_id,
         p.company_id, 
         sp.user_id recipient_id,
        li.om_level_name,
         cy.awardbanq_country_abbrev country,
         sp.recipientstatus award_status,
         CASE WHEN mo.is_redeemed = 1 THEN 'Redeemed'
            WHEN mo.is_redeemed = 0 AND mo.order_status = 'invalid' THEN 'Cancelled'
            WHEN mo.is_redeemed = 0 AND mo.order_status = 'expired' THEN 'Expired'
            WHEN mo.is_redeemed = 0 THEN 'Issued'
            ELSE 'Scheduled'
         END gift_code_status,
         null award_points,
         0 is_taxable,
         null points_status,
         sp.awarddate,
         rc.team_id,
         1 is_giftcode,
         0 is_points,
         1 is_celebration_site,
         1 is_daymaker,
         5662 createdby,
         sp.createdat,
         CASE WHEN sp.updatedat IS NOT NULL THEN 5662 
          ELSE NULL END modified_by,
         sp.updatedat,
         0 version
    FROM stg_sa_programs1 ssp,
         program p,
         stg_sa_recipients1 sp,
         merch_order mo,
         promo_merch_program_level pmpl,
         promo_merch_country pmc,
         recognition_claim rc,
         (select om_level_name, level_id,id from
(SELECT distinct to_number(sr.level_id) level_id,sr.RECIPIENT_ID,ssp.id
                    FROM stg_sa_recipients1 ssp,
                         scheduled_recognition sr,
                         stg_sa_programs1 p
                   WHERE ssp.user_id = sr.recipient_id
                   AND sr.promotion_id = p.programid
                   AND user_type = 'schedule_celebrator'
                  union   
                  SELECT cr.promo_merch_program_level_id level_id,cr.PARTICIPANT_ID,ssp.id
                    FROM stg_sa_recipients1 ssp,
                         claim c,
                         claim_item ci,
                         claim_recipient cr
                   WHERE ssp.claim_id = c.claim_id
                     AND c.claim_id = ci.claim_id
                     AND ci.claim_item_id = cr.claim_item_id
                     AND user_type = 'claim_celebrator'
                     union   
                     SELECT pr.AWARD_LEVEL_ID, pr.user_id,ssp.id
                       FROM stg_sa_recipients1 ssp,
                            purl_recipient pr
                      WHERE ssp.purl_recipient_id = pr.purl_recipient_id
                        AND user_type = 'purl_celebrator') li,
                        PROMO_MERCH_PROGRAM_LEVEL pmpl
                      WHERE level_id = promo_merch_program_level_id) li,
                (select ua.country_id,
                       ua.user_id,
                       c.awardbanq_country_abbrev 
                from user_address ua, 
                     country c 
                where c.country_id=ua.country_id and ua.is_primary=1
                ) cy                     
   WHERE ssp.program_uuid = p.external_program_id
     AND ssp.program_uuid = sp.programid
     AND sp.claim_id = mo.claim_id(+)
     and sp.id = li.id 
     and sp.user_id = cy.user_id(+)
     AND li.level_id = pmpl.promo_merch_program_level_id(+)
     AND mo.promo_merch_program_level_id = pmpl.promo_merch_program_level_id(+)
     AND pmpl.promo_merch_country_id = pmc.promo_merch_country_id(+)
     AND sp.claim_id = rc.claim_id(+)) s;
      
v_stage := 'Populate data into sa_inviteandcontribute_info';  
INSERT INTO sa_inviteandcontribute_info    
SELECT sa_inviteandcontrib_info_pk_sq.nextval,
       sr.recipientid,
       pc.purl_contributor_id,
       au.user_id contributor_person_id,
       au.first_name contributor_first_name,
       au.last_name contributor_last_name,
       ue.email_addr,
       pc.date_created invite_send_date,
       invite_contributor_id invitee_person_id,
       CASE WHEN  pc.state = 'invitation' THEN 'invited' ELSE 'contributed' END contribution_state,
       1 is_external_or_internal,
       1 is_invited,
       null contributed_date,
       pc.created_by,
       pc.date_created,
       pc.modified_by,
       pc.date_modified,
       pc.version
  FROM stg_sa_recipients1 sr,
       purl_contributor pc,
       application_user au,
       user_email_address ue
 WHERE sr.purl_recipient_id = pc.purl_recipient_id
   AND pc.user_id = au.user_id
   AND au.user_id = ue.user_id
   AND ue.is_primary = 1;    
   
v_stage := 'Copying data to source on-prem informatica schema stg tables';     
prc_stg_data_migration;   

v_stage := 'Update scheduled records';
UPDATE scheduled_recognition
   SET is_fired = 1,
       date_modified = sysdate,
       modified_by = 5662,
       version = version+1
  WHERE is_fired = 0
    AND promotion_id IN (SELECT programid
                           FROM stg_sa_programs1);   

prc_execution_log_entry (c_process_name,c_release_level,'INFO','Procedure completed successfully' ,NULL);
COMMIT;      
EXCEPTION
  WHEN OTHERS THEN
       prc_execution_log_entry (c_process_name,c_release_level,
                                'Error',v_stage || SQLCODE || ':' || SQLERRM,NULL);
      ROLLBACK;    
END prc_sa_datamigration;
/