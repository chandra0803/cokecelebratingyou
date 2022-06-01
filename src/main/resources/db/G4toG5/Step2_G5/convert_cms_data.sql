DECLARE
   /*-----------------------------------------------------------------------------
     Purpose: Create cms records for promotion.promo_name_asset_code

     Modification History
     Person                      Date         Comments
     -----------               ----------   ---------------------------------------------------
     Ravi Dhanekula  02/13/2012   Initial creation
     Arun S          03/04/2013   Modified Insert into cms_asset_type_item to use cms_asset_type.id (current value)

   -----------------------------------------------------------------------------*/

   v_cms_application_id       cms_application.id%TYPE;
   v_cms_asset_type_id        cms_asset_type.id%TYPE;
   v_cms_section_id           cms_section.id%TYPE;
   v_cms_asset_id             cms_asset.id%TYPE;
   v_name_asset_code_id       NUMBER;
   v_cms_content_id           cms_content.id%TYPE;
   v_cms_content_key_id       cms_content_key.id%TYPE;
   v_stage                    VARCHAR2 (100);
   v_cms_asset_type_item_id   cms_asset_type_item.id%TYPE;
BEGIN
   FOR rec_cms_section IN (SELECT id,
                                  entity_version,
                                  code,
                                  description,
                                  name,
                                  application_id,
                                  created_by,
                                  date_created,
                                  modified_by,
                                  date_modified
                             FROM cms_section
                            WHERE code IN ('hierarchy_data',
                                           'message_data',
                                           'supplier_awards_page',
                                           'node_type_data',
                                           'characteristic_data',
                                           'picklist',
                                           'budget_master_data',
                                           'quiz_question_data',
                                           'quiz_question_answer',
                                           'claim_form_data',
                                           'promotion_data',
                                           'spotlight_levels_data',
                                           'calculator_criterion_data',
                                           'calculator_criterion_rating'))
   LOOP
      SELECT id
        INTO v_cms_section_id
        FROM cms_section
       WHERE code = rec_cms_section.code;

      IF rec_cms_section.CODE <> 'picklist'
      THEN
         --           insert into cms_asset_type
         --             (id,
         --             entity_version,
         --             name,
         --             description,
         --             is_previewable,
         --             is_multi,
         --             is_display,
         --             application_id,
         --             created_by,
         --             date_created,
         --             modified_by,
         --             date_modified)  (
         --            SELECT cms_generic_pk_sq.NEXTVAL,entity_version,
         --             name,
         --             description,
         --             is_previewable,
         --             is_multi,
         --             is_display,
         --             application_id,
         --             created_by,
         --             date_created,
         --             modified_by,
         --             date_modified FROM
         --            cms_asset_type_G4 cmt where id in (select asset_type_id from cms_asset_G4 where section_id in (select id from cms_section_g4 where code=rec_cms_section.code))
         --            and not exists (select * from cms_asset_type where name=cmt.name));

         FOR rec_asset_type
            IN (SELECT id,
                       entity_version,
                       name,
                       description,
                       is_previewable,
                       is_multi,
                       is_public,
                       is_display,
                       application_id,
                       created_by,
                       date_created,
                       modified_by,
                       date_modified
                  FROM cms_asset_type_G4 cmt
                 WHERE     id IN (SELECT asset_type_id
                                    FROM cms_asset_G4
                                   WHERE section_id IN (SELECT id
                                                          FROM cms_section_g4
                                                         WHERE code =
                                                                  rec_cms_section.code))
                       AND NOT EXISTS
                                  (SELECT *
                                     FROM cms_asset_type
                                    WHERE     name = cmt.name
                                          AND application_id =
                                                 cmt.application_id))
         LOOP
            v_stage := 'insert into cms_asset_type' || rec_asset_type.name;

            INSERT INTO cms_asset_type (id,
                                        entity_version,
                                        name,
                                        description,
                                        is_previewable,
                                        is_multi,
                                        is_public,
                                        is_display,
                                        application_id,
                                        created_by,
                                        date_created,
                                        modified_by,
                                        date_modified)
                 VALUES (cms_generic_pk_sq.NEXTVAL,
                         rec_asset_type.entity_version,
                         rec_asset_type.name,
                         rec_asset_type.description,
                         rec_asset_type.is_previewable,
                         rec_asset_type.is_multi,
                         rec_asset_type.is_public,
                         rec_asset_type.is_display,
                         rec_asset_type.application_id,
                         rec_asset_type.created_by,
                         rec_asset_type.date_created,
                         rec_asset_type.modified_by,
                         rec_asset_type.date_modified)
              RETURNING id
                   INTO v_cms_asset_type_item_id;

            INSERT INTO cms_asset_type_item (id,
                                             entity_version,
                                             key,
                                             name,
                                             description,
                                             is_required,
                                             is_unique,
                                             is_translatable,
                                             sort_order,
                                             asset_type_id,
                                             data_type,
                                             max_length,
                                             min_length,
                                             created_by,
                                             date_created,
                                             modified_by,
                                             date_modified)
               (SELECT cms_generic_pk_sq.NEXTVAL,
                       entity_version,
                       key,
                       name,
                       description,
                       is_required,
                       is_unique,
                       is_translatable,
                       sort_order,
                       v_cms_asset_type_item_id,              --asset_type_id,
                       data_type,
                       max_length,
                       min_length,
                       created_by,
                       date_created,
                       modified_by,
                       date_modified
                  FROM cms_asset_type_item_G4
                 WHERE asset_type_id = rec_asset_type.id);
         END LOOP;

         INSERT INTO cms_asset (id,
                                entity_version,
                                product_version,
                                code,
                                name,
                                description,
                                preview_page,
                                is_public,
                                section_id,
                                asset_type_id,
                                parent_asset_id,
                                created_by,
                                date_created,
                                modified_by,
                                date_modified,
                                pax_or_admin,
                                module)
            (SELECT cms_generic_pk_sq.NEXTVAL,
                    cs.entity_version,
                    '4.2.2',
                    cs.code,
                    cs.name,
                    cs.description,
                    cs.preview_page,
                    cs.is_public,
                    v_cms_section_id,
                    cat.id,
                    cs.parent_asset_id,
                    cs.created_by,
                    cs.date_created,
                    cs.modified_by,
                    cs.date_modified,
                    NVL (cs.pax_or_admin, 'B'),
                    NVL (cs.module, 'core')
               FROM cms_asset_g4 cs,
                    cms_asset_type cat,
                    cms_asset_type_g4 catg4
              WHERE     section_id IN (SELECT id
                                         FROM cms_section_g4
                                        WHERE code = rec_cms_section.code)
                    AND NOT EXISTS
                               (SELECT *
                                  FROM cms_asset
                                 WHERE     code = cs.code
                                       AND section_id = v_cms_section_id)
                    AND cs.asset_type_id = catg4.id
                    AND catg4.name = cat.name
                    AND cat.application_id = rec_cms_section.application_id);
      END IF;


      INSERT INTO cms_audience (id,
                                entity_version,
                                code,
                                description,
                                name,
                                application_id,
                                created_by,
                                date_created,
                                modified_by,
                                date_modified)
         (SELECT cms_generic_pk_sq.NEXTVAL,
                 entity_version,
                 code,
                 description,
                 name,
                 application_id,
                 created_by,
                 date_created,
                 modified_by,
                 date_modified
            FROM cms_audience_g4 cag4
           WHERE     application_id = rec_cms_section.application_id
                 AND NOT EXISTS
                            (SELECT *
                               FROM cms_audience
                              WHERE     application_id =
                                           rec_cms_section.application_id
                                    AND (name = cag4.name OR code = cag4.code)));


      --            INSERT INTO cms_content_key
      --            (id,entity_version,sort_order,guid,asset_id,start_timestamp,end_timestamp,created_by,date_created,modified_by,date_modified,filter_string)
      --            (select cms_generic_pk_sq.NEXTVAL,cck.entity_version,cck.sort_order,cck.guid,ca.id,cck.start_timestamp,cck.end_timestamp,cck.created_by,cck.date_created,cck.modified_by,cck.date_modified,cck.filter_string
      --               from cms_content_key_g4 cck,cms_asset ca,cms_asset_g4 cag4
      --              where asset_id in (select id
      --                                   from cms_asset_G4
      --                                  where section_id in (select id
      --                                                         from cms_section_G4
      --                                                        where code=rec_cms_section.code))
      --                and not exists (select *
      --                                  from cms_content_key
      --                                 where guid=cck.guid)
      --                and cck.asset_id=cag4.id
      --                and cag4.code=ca.code);


      FOR rec_content_key
         IN (SELECT cck.id,
                    cck.entity_version,
                    cck.sort_order,
                    DECODE(cck.guid,'194e776:106324a3f0c:-7b3e','994e776:106324a3f0c:-7b3e',cck.guid) guid,
                    ca.id asset_id,
                    cck.start_timestamp,
                    cck.end_timestamp,
                    cck.created_by,
                    cck.date_created,
                    cck.modified_by,
                    cck.date_modified,
                    cck.filter_string
               FROM cms_content_key_g4 cck, cms_asset ca, cms_asset_g4 cag4
              WHERE     asset_id IN (SELECT id
                                       FROM cms_asset_G4
                                      WHERE section_id IN (SELECT id
                                                             FROM cms_section_G4
                                                            WHERE code =
                                                                     rec_cms_section.code))
                    AND ca.code NOT IN ('picklist.promotion.awardstype.items',
                                        'picklist.promo.email.notification.type.items',
                                        'picklist.promo.notification.type',
                                        'picklist.promo.notification.type.items')
                    AND NOT EXISTS
                           (SELECT *
                              FROM cms_content_key
                             WHERE guid = DECODE(cck.guid,'194e776:106324a3f0c:-7b3e','994e776:106324a3f0c:-7b3e',cck.guid))
                    AND cck.asset_id = cag4.id
                    AND cag4.code = ca.code)
      LOOP
         v_stage := 'insert into cms_content_key';

         INSERT INTO cms_content_key (id,
                                      entity_version,
                                      sort_order,
                                      guid,
                                      asset_id,
                                      start_timestamp,
                                      end_timestamp,
                                      created_by,
                                      date_created,
                                      modified_by,
                                      date_modified,
                                      filter_string)
              VALUES (cms_generic_pk_sq.NEXTVAL,
                      rec_content_key.entity_version,
                      rec_content_key.sort_order,
                      rec_content_key.guid,
                      rec_content_key.asset_id,
                      rec_content_key.start_timestamp,
                      rec_content_key.end_timestamp,
                      rec_content_key.created_by,
                      rec_content_key.date_created,
                      rec_content_key.modified_by,
                      rec_content_key.date_modified,
                      rec_content_key.filter_string)
           RETURNING id
                INTO v_cms_content_key_id;

         INSERT INTO CMS_CONTENT_KEY_AUDIENCE_LNK (content_key_id,
                                                   audience_id)
            (SELECT v_cms_content_key_id,
                    (SELECT ca.id
                       FROM cms_audience_g4 cag4, cms_audience ca
                      WHERE     cag4.id = cckalg4.audience_id
                            AND cag4.code = ca.code
                            AND cag4.name = ca.name
                            AND ca.application_id =
                                   rec_cms_section.application_id)
               FROM cms_content_key_audience_ln_g4 cckalg4
              WHERE content_key_id = rec_content_key.id);
      END LOOP;


      INSERT INTO cms_content (id,
                               entity_version,
                               locale,
                               version,
                               content_status,
                               edited_by,
                               edited_timestamp,
                               added_by,
                               added_timestamp,
                               submitted_by,
                               submitted_timestamp,
                               approved_by,
                               approved_timestamp,
                               guid,
                               content_key_id,
                               export_date,
                               content_version,
                               created_by,
                               date_created,
                               modified_by,
                               date_modified)
         (SELECT cms_generic_pk_sq.NEXTVAL,
                 cc.entity_version,
                 DECODE (cc.locale, 'en', 'en_US', cc.locale),
                 cc.version,
                 cc.content_status,
                 cc.edited_by,
                 cc.edited_timestamp,
                 cc.added_by,
                 cc.added_timestamp,
                 cc.submitted_by,
                 cc.submitted_timestamp,
                 cc.approved_by,
                 cc.approved_timestamp,
                 cc.guid,
                 cck.id,
                 cc.export_date,
                 1,
                 cc.created_by,
                 cc.date_created,
                 cc.modified_by,
                 cc.date_modified
            FROM cms_content_G4 cc,
                 cms_content_key cck,
                 cms_content_key_g4 cckg4,
                 cms_asset_g4 cag4
           WHERE     cckg4.asset_id IN (SELECT id
                                          FROM cms_asset_g4
                                         WHERE section_id IN (SELECT id
                                                                FROM cms_section_g4
                                                               WHERE code =
                                                                        rec_cms_section.code))
                 AND NOT EXISTS
                        (SELECT *
                           FROM cms_content
                          WHERE guid = cc.guid)
                 AND cc.content_key_id = cckg4.id
--                 AND cckg4.guid = cck.guid
                 AND DECODE(cckg4.guid,'194e776:106324a3f0c:-7b3e','994e776:106324a3f0c:-7b3e',cckg4.guid) = cck.guid
                 AND cckg4.asset_id = cag4.id
                 AND cag4.code NOT IN ('picklist.promotion.awardstype.items',
                                       'picklist.promo.email.notification.type.items',
                                       'picklist.promo.notification.type',
                                       'picklist.promo.notification.type.items'));

--      INSERT INTO cms_content_data (content_id,
--                                    VALUE,
--                                    key,
--                                    google_value)
--         (SELECT cc.id content_id,
--                 ccdg4.VALUE,
--                 ccdg4.key,
--                 ccdg4.google_value
--            FROM cms_content_data_g4 ccdg4,
--                 cms_content_g4 ccg4,
--                 cms_content cc,
--                 cms_content_key_g4 cckg4,
--                 cms_asset_g4 cag4
--           WHERE     cckg4.asset_id IN (SELECT id
--                                          FROM cms_asset_g4
--                                         WHERE section_id IN (SELECT id
--                                                                FROM cms_section_g4
--                                                               WHERE code =
--                                                                        rec_cms_section.code))
--                 AND cckg4.id = CCG4.CONTENT_KEY_ID
--                 AND ccg4.id = ccdg4.content_id
--                 AND cc.guid = CCG4.GUID
--                 AND cckg4.asset_id = cag4.id
--                 AND cag4.code NOT IN ('picklist.promotion.awardstype.items',
--                                       'picklist.promo.email.notification.type.items',
--                                       'picklist.promo.notification.type',
--                                       'picklist.promo.notification.type.items')
--                 and UPPER(DBMS_LOB.substr(ccdg4.value,300,1)) NOT IN ('AWARDPERQS','AWARDPERQ')
--                  AND  NOT EXISTS
--                        (SELECT *
--                           FROM cms_content_data
--                          WHERE content_id = cc.id AND key = ccdg4.key AND UPPER(DBMS_LOB.substr(value,300,1))  =UPPER(DBMS_LOB.substr(ccdg4.value,300,1)) )
--                          );

        MERGE INTO cms_content_data d
   USING (  -- build node/child list based on the node connect by path
            SELECT cc.id content_id,                 
                 ccdg4.VALUE,
                 ccdg4.key,
                 ccdg4.google_value
            FROM cms_content_data_g4 ccdg4,
                 cms_content_g4 ccg4,
                 cms_content cc,
                 cms_content_key_g4 cckg4,
                 cms_asset_g4 cag4
           WHERE     cckg4.asset_id IN (SELECT id
                                          FROM cms_asset_g4
                                         WHERE section_id IN (SELECT id
                                                                FROM cms_section_g4
                                                               WHERE code =
                                                                        rec_cms_section.code))
                 AND cckg4.id = CCG4.CONTENT_KEY_ID
                 AND ccg4.id = ccdg4.content_id
                 AND cc.guid = CCG4.GUID
                 AND cckg4.asset_id = cag4.id
                 AND cag4.code NOT IN ('picklist.promotion.awardstype.items',
                                       'picklist.promo.email.notification.type.items',
                                       'picklist.promo.notification.type',
                                       'picklist.promo.notification.type.items')
                 and UPPER(DBMS_LOB.substr(ccdg4.value,300,1)) NOT IN ('AWARDPERQS','AWARDPERQ')   
                ) s
      ON (   d.content_id = s.content_id
         AND d.key = s.key)
     WHEN MATCHED THEN UPDATE 
         SET value = s.value,
                google_value = s.google_value
    WHEN NOT MATCHED THEN
      INSERT
      ( content_id,
        VALUE,
        key,
        google_value
      )
      VALUES
      ( s.content_id,
        s.VALUE,
        s.key,
        s.google_value
      );
   END LOOP;                                             --rec_cms_section END

   --      delete  FROM cms_content_data WHERE content_id IN ( --Added to delete duplicate content_data entries for picklists.
   --SELECT content_id FROM (SELECT RANK() OVER (PARTITION BY code,to_char(value),locale ORDER BY content_id DESC) as rec_rank,
   --               pe.* FROM (
   --select ca.code,ccd.value,ccd.content_id,cc.locale from
   -- cms_content_data ccd,
   --cms_content cc,
   --cms_content_key cck,
   --cms_asset ca
   --WHERE
   --ca.code like 'picklist%'
   --AND ca.id = cck.asset_id
   --AND cck.id = cc.content_key_id
   --AND cc.id = ccd.content_id
   --AND ccd.key='CODE') pe) WHERE rec_rank=2);
   
UPDATE cms_content_data
   SET VALUE = 'points'
 WHERE TO_CHAR (DBMS_LOB.SUBSTR (VALUE, 300, 1)) = 'awardperqs';
 
UPDATE cms_content_data
SET value='Points'
WHERE to_char(DBMS_LOB.substr(value, 300, 1))='AwardperQs';

UPDATE cms_content_data
   SET VALUE = 'points'
 WHERE TO_CHAR (DBMS_LOB.SUBSTR (VALUE, 300, 1)) = 'awardperq';
 
 UPDATE cms_content_data
SET value='Points'
WHERE to_char(DBMS_LOB.substr(value, 300, 1))='Awardperqs';

   DBMS_OUTPUT.put_line ('Convert CMS data completed');
EXCEPTION
   WHEN OTHERS
   THEN
      DBMS_OUTPUT.put_line (v_stage || SQLERRM);
END;
/